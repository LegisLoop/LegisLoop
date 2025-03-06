package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.repository.LegislationRepository;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
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

    public List<Legislation> getMasterListChange(String state) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getMasterListRaw")
                .queryString("state", state)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonObject masterlistObject = jsonObject.getAsJsonObject("masterlist");

                Type mapType = new TypeToken<Map<String, Legislation>>() {}.getType();
                Map<String, Legislation> billsMap = gson.fromJson(masterlistObject, mapType);

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

                Gson gson = new Gson();
                JsonObject billObject = jsonObject.getAsJsonObject("bill");

                Legislation incomingLegislation = gson.fromJson(billObject, Legislation.class);
                final Legislation effectiveLegislation = 
                	    (incomingLegislation.getChange_hash() != legislation.getChange_hash()) 
                	        ? incomingLegislation 
                	        : legislation;

                JsonArray textsArray = billObject.getAsJsonArray("texts");
                JsonArray sponsorsArray = billObject.getAsJsonArray("sponsors");
                JsonArray votesArray = billObject.getAsJsonArray("votes");
                
                List<RollCall> rollCalls = new ArrayList<RollCall>();
                votesArray.forEach(roll_call -> {
                	rollCalls.add(RollCallService.fillRecord(roll_call.getAsJsonObject()));
                });
                effectiveLegislation.setRoll_calls(rollCalls); // TODO: Add these to the repository

                textsArray.forEach(text -> {
                	// TODO: Refactor this shit as GSON or a fill_record (or both!)
                    LegislationDocument possibleNewLegislationDocument = LegislationDocument.builder()
                            .billId(effectiveLegislation.getBill_id())
                            .textHash(text.getAsJsonObject().get("text_hash").getAsString())
                            .legiscanLink(URI.create(text.getAsJsonObject().get("url").getAsString()))
                            .externalLink(URI.create(text.getAsJsonObject().get("state_link").getAsString()))
                            .docId(text.getAsJsonObject().get("doc_id").getAsInt())
                            .mime(text.getAsJsonObject().get("mime").getAsString())
                            .mimeId(text.getAsJsonObject().get("mime_id").getAsInt())
                            .type(text.getAsJsonObject().get("type").getAsString())
                            .typeId(text.getAsJsonObject().get("type_id").getAsInt())
                            .build();

                    List<LegislationDocument> documents = effectiveLegislation.getDocuments();
                    boolean replacedDoc = false;
                    if (documents != null) {
	                    for (LegislationDocument document : documents) {
	                        if (document.getDocId() == possibleNewLegislationDocument.getDocId()) {
	                            if (Objects.equals(document.getTextHash(), possibleNewLegislationDocument.getTextHash())) break;
	                            effectiveLegislation.documents.remove(document);
	                            effectiveLegislation.documents.add(possibleNewLegislationDocument); // TODO: Fetch the actual doc content for this doc before adding it
	                            replacedDoc = true;
	                            break;
	                        }
	                    }
	
	                    if (!replacedDoc) effectiveLegislation.documents.add(possibleNewLegislationDocument); // TODO: Add this document to the repository
                    }

                });
                
                List<Representative> billSponsors = effectiveLegislation.getSponsors();
                if (billSponsors != null) {
	                sponsorsArray.forEach(sponsor -> {
	                    int peopleId = sponsor.getAsJsonObject().get("people_id").getAsInt();
	                    boolean exists = billSponsors.stream().anyMatch(rep -> rep.getPeople_id() == peopleId);
	                    if (!exists) {
	                    	// TODO: Refactor this shit as GSON or a fill_record (or both!)
	                    	// TODO: I think this is missing middle_name, suffix, etc., giving more necessity to the refactor!
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
	                        billSponsors.add(representativeToAdd); // TODO: Update this with RepresentativeService.update(representativeToAdd, fromTheDBrep)
	                    }
	                });
                }
                
                effectiveLegislation.setStateLink(new URI(jsonObject.getAsJsonObject("bill").get("state_link").getAsString()));

                int stateId = Integer.parseInt(jsonObject.getAsJsonObject("bill").get("state_id").getAsString());
                effectiveLegislation.setState(StateEnum.fromStateID(stateId));
                
                legislation = effectiveLegislation; // TODO: Make an update function for legislation.
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
    //TODO delete eventually
    public List<Legislation> getAllLegislation() {
        return legislationRepository.findAll().stream().map(LegislationEntity::toModel).toList();
    }
    //TODO delete eventually
    public Legislation getLegislationById(int bill_id) {
        return legislationRepository.getReferenceById(bill_id).toModel();
    }

}
