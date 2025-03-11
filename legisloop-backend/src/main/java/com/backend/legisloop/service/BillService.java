package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.util.Utils;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.LegislationDocument;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.model.RollCall;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillService {

    private final LegislationRepository legislationRepository;
    private final RepresentativeRepository representativeRepository;

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;

    public List<Legislation> getMasterList(String state) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getMasterList")
                .queryString("state", state)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonObject masterlistObject = jsonObject.getAsJsonObject("masterlist");
                JsonObject sessionObject = masterlistObject.getAsJsonObject("session");
                masterlistObject.remove("session");

                Type mapType = new TypeToken<Map<String, Legislation>>() {}.getType();
                Map<String, Legislation> billsMap = gson.fromJson(masterlistObject, mapType);

                int sessionId = sessionObject.get("session_id").getAsInt();
                billsMap.values().forEach(legislation -> legislation.setSession_id(sessionId));

                return new ArrayList<>(billsMap.values());
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }

    /**
     * Get a complete {@link Legislation} from a 'stub' (presumably from {@link #getMasterList(String)}).
     * @param legislation The {@link Legislation} with, at minimum, a filled {@link Legislation#bill_id}
     * @return A completed {@link Legislation} with texts, sans {@link LegislationDocument#docContent}
     * @throws UnirestException
     * @throws URISyntaxException
     */
    public Legislation getBill(Legislation legislation) throws UnirestException, URISyntaxException { // NOTE: it is assumed that the state is known from first calling master list function
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getBill")
                .queryString("id", legislation.getBill_id())
                .asJson();

        if (response.getStatus() == 200) {
            try {
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonArray textsArray = jsonObject.getAsJsonObject("bill").getAsJsonArray("texts");
                JsonArray sponsorsArray = jsonObject.getAsJsonObject("bill").getAsJsonArray("sponsors");
                JsonArray votesArray = jsonObject.getAsJsonObject("bill").getAsJsonArray("votes");
                
                List<RollCall> rollCalls = new ArrayList<RollCall>();
                votesArray.forEach(roll_call -> {
                	rollCalls.add(RollCallService.fillRecord(roll_call.getAsJsonObject()));
                });
                legislation.setRoll_calls(rollCalls);

                textsArray.forEach(text -> {
                    LegislationDocument possibleNewLegislationDocument = LegislationDocument.builder()
                            .billId(legislation.getBill_id())
                            .textHash(text.getAsJsonObject().get("text_hash").getAsString())
                            .legiscanLink(URI.create(text.getAsJsonObject().get("url").getAsString()))
                            .externalLink(URI.create(text.getAsJsonObject().get("state_link").getAsString()))
                            .docId(text.getAsJsonObject().get("doc_id").getAsInt())
                            .mime(text.getAsJsonObject().get("mime").getAsString())
                            .mimeId(text.getAsJsonObject().get("mime_id").getAsInt())
                            .type(text.getAsJsonObject().get("type").getAsString())
                            .typeId(text.getAsJsonObject().get("type_id").getAsInt())
                            .build();

                    List<LegislationDocument> documents = legislation.getDocuments();
                    boolean replacedDoc = false;
                    if (documents != null) {
	                    for (LegislationDocument document : documents) {
	                        if (document.getDocId() == possibleNewLegislationDocument.getDocId()) {
	                            if (Objects.equals(document.getTextHash(), possibleNewLegislationDocument.getTextHash())) break;
	                            legislation.documents.remove(document);
	                            legislation.documents.add(possibleNewLegislationDocument); // TODO: Should refetech the doc content for this doc
	                            replacedDoc = true;
	                            break;
	                        }
	                    }
	
	                    if (!replacedDoc) legislation.documents.add(possibleNewLegislationDocument);
                    }

                });
                
                List<Representative> billSponsors = legislation.getSponsors();
                if (billSponsors != null) {
	                sponsorsArray.forEach(sponsor -> {
	                    int peopleId = sponsor.getAsJsonObject().get("people_id").getAsInt();
	                    boolean exists = billSponsors.stream().anyMatch(rep -> rep.getPeople_id() == peopleId);
	                    if (!exists) {
	                        Representative representativeToAdd = Representative.builder()
	                                .people_id(sponsor.getAsJsonObject().get("people_id").getAsInt())
	                                .person_hash(sponsor.getAsJsonObject().get("person_hash").getAsString())
	                                .party_id(sponsor.getAsJsonObject().get("party_id").getAsInt())
	                                .state_id(sponsor.getAsJsonObject().get("state_id").getAsInt())
	                                .party(sponsor.getAsJsonObject().get("party").getAsString())
	                                .role_id(sponsor.getAsJsonObject().get("role_id").getAsInt())
	                                .role(sponsor.getAsJsonObject().get("role").getAsString())
	                                .name(sponsor.getAsJsonObject().get("name").getAsString())
	                                .first_name(sponsor.getAsJsonObject().get("first_name").getAsString())
	                                .last_name(sponsor.getAsJsonObject().get("last_name").getAsString())
	                                .district(sponsor.getAsJsonObject().get("district").getAsString())
	                                .ftm_eid(sponsor.getAsJsonObject().get("ftm_eid").getAsInt())
	                                .votesmart_id(sponsor.getAsJsonObject().get("votesmart_id").getAsInt())
	                                .knowwho_pid(sponsor.getAsJsonObject().get("knowwho_pid").getAsInt())
	                                .build();
	                        billSponsors.add(representativeToAdd);
	                    }
	                });
                }
                
                legislation.setStateLink(new URI(jsonObject.getAsJsonObject("bill").get("state_link").getAsString()));

                int stateId = Integer.parseInt(jsonObject.getAsJsonObject("bill").get("state_id").getAsString());
                legislation.setState(StateEnum.fromStateID(stateId));
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
        return legislation;
    }
    
    private LegislationDocument getDocContent(LegislationDocument legislationDocument) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getBillText")
                .queryString("id", legislationDocument.getDocId())
                .asJson();

        if (response.getStatus() == 200) {
            JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
            Utils.checkLegiscanResponseStatus(jsonObject);

            JsonObject text = jsonObject.getAsJsonObject("text");

            legislationDocument.setMime(text.getAsJsonObject("mime").getAsString());
            legislationDocument.setMimeId(text.getAsJsonObject("mime_id").getAsInt());
            legislationDocument.setTypeId(text.getAsJsonObject("type_id").getAsInt());
            legislationDocument.setType(text.getAsJsonObject("type").getAsString());
            legislationDocument.setDocContent(text.getAsJsonObject("doc").getAsString());

            return legislationDocument;


        }
        log.error("Failed to fetch bill text");
        return legislationDocument;
    }

    public Page<LegislationEntity> getLegislationByState(StateEnum state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return legislationRepository.findByState(state, pageable);
    }

    public Page<Legislation> getLegislationByStateId(int stateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        StateEnum state = StateEnum.fromStateID(stateId);
        Page<LegislationEntity> legislationEntities = legislationRepository.findByState(state, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }

    public Page<Legislation> getLegislationByRepresentativeIdPaginated(int repId, int page, int size) {
        RepresentativeEntity representative = representativeRepository.findById(repId)
                .orElseThrow(() -> new EntityNotFoundException("Representative not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<LegislationEntity> legislationEntities = legislationRepository.findBySponsors(representative, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }
    public Page<Legislation> getLegislationBySessionIdPaginated(int sessionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LegislationEntity> legislationEntities = legislationRepository.findBySessionId(sessionId, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }
}
