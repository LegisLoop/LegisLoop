package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationDocumentEntity;
import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.entities.RollCallEntity;
import com.backend.legisloop.entities.VoteEntity;
import com.backend.legisloop.repository.LegislationDocumentRepository;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.repository.RollCallRepository;
import com.backend.legisloop.repository.VoteRepository;
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
import jakarta.annotation.PostConstruct;
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
    private final RepresentativeRepository representativeRepository;
    private final LegislationDocumentRepository legislationDocumentRepository;
    private final RollCallRepository rollCallRepository;
    private final VoteRepository voteRepository;

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

    @PostConstruct
    public void insertDummyData() {
        // Insert Representatives
        RepresentativeEntity rep1 = RepresentativeEntity.builder()
                .peopleId(1)
                .name("John Doe")
                .party("Democrat")
                .stateId(1)
                .role("Senator")
                .roleId(1)
                .build();

        RepresentativeEntity rep2 = RepresentativeEntity.builder()
                .peopleId(2)
                .name("Jane Smith")
                .party("Republican")
                .stateId(2)
                .role("Representative")
                .roleId(1)
                .build();

        representativeRepository.saveAll(Arrays.asList(rep1, rep2));
        
        RollCallEntity rollCall1 = RollCallEntity.builder()
        		.bill_id(101)
        		.absent(1)
        		.nv(2)
        		.yea(3)
        		.nay(4)
        		.passed(false)
        		.total(7)
        		.desc("Baller vote")
        		.roll_call_id(999)
        		.build();
        
        RollCallEntity rollCall2 = RollCallEntity.builder()
        		.bill_id(101)
        		.absent(5)
        		.nv(6)
        		.yea(7)
        		.nay(8)
        		.passed(false)
        		.total(15)
        		.desc("Mega baller vote")
        		.roll_call_id(1000)
        		.build();
        
        rollCallRepository.saveAll(Arrays.asList(rollCall1, rollCall2));

        // Insert Legislation
        LegislationEntity legislation = LegislationEntity.builder()
                .billId(101)
                .title("Clean Energy Act")
                .description("A bill to promote renewable energy")
                .summary("This bill provides incentives for clean energy projects.")
                .change_hash("xyz123")
                .url("https://example.com/legislation/101")
                .stateLink("https://state.example.com/legislation/101")
                .sponsors(List.of(rep1, rep2))
                .endorsements(List.of(rep1))
                .rollCalls(List.of(rollCall1, rollCall2))
                .build();

        legislationRepository.save(legislation);

        // Insert Legislation Documents
        LegislationDocumentEntity document = LegislationDocumentEntity.builder()
                .docId(1)
                .bill(legislation)
                .textHash("doc_hash_123")
                .legiscanLink(URI.create("https://example.com/document/1"))
                .externalLink(URI.create("https://external.example.com/document/1"))
                .mime("application/pdf")
                .mimeId(1)
                .docContent("Sample document content")
                .type("Bill Text")
                .typeId(101)
                .build();

        legislationDocumentRepository.save(document);
    }

    public List<Legislation> getAllLegislation() {
        return legislationRepository.findAll().stream().map(LegislationEntity::toModel).toList();
    }
    public Legislation getLegislationById(int bill_id) {
        return legislationRepository.getReferenceById(bill_id).toModel();
    }

}
