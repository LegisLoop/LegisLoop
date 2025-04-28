package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.entities.RollCallEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.repository.LegislationDocumentRepository;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.repository.RollCallRepository;
import com.backend.legisloop.serial.DateSerializer;
import com.backend.legisloop.util.Utils;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.LegislationDocument;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.model.RollCall;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LegislationService {

    private final LegislationRepository legislationRepository;
    private final LegislationDocumentRepository legislationDocumentRepository;
    private final RepresentativeRepository representativeRepository;
    private final RollCallRepository rollCallRepository;

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
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.sql.Date.class, new DateSerializer()) // Register adapter
                        .create();
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
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.sql.Date.class, new DateSerializer()) // Register adapter
                        .create();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonObject masterlistObject = jsonObject.getAsJsonObject("masterlist");

                Type mapType = new TypeToken<Map<String, Legislation>>() {}.getType();
                Map<String, Legislation> billsMap = gson.fromJson(masterlistObject, mapType);
                billsMap.remove("session"); // remove the session identifier object

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
     * @apiNote LegiScan called.
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

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.sql.Date.class, new DateSerializer()) // Register adapter
                        .create();
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
                	RollCall rollCallToAdd = RollCall.fillRecord(roll_call.getAsJsonObject());
                	rollCallToAdd.setBill_id(effectiveLegislation.getBill_id());
                	rollCalls.add(rollCallToAdd);
                });
                effectiveLegislation.setRoll_calls(rollCalls);
                updateRollCalls(rollCalls);

                textsArray.forEach(text -> {
                    LegislationDocument possibleNewLegislationDocument = LegislationDocument.fillDocument(text.getAsJsonObject());
                    possibleNewLegislationDocument.setBillId(effectiveLegislation.getBill_id());

                    List<LegislationDocument> documents = effectiveLegislation.getDocuments();
                    boolean replacedDoc = false;
                    if (documents != null) {
	                    for (LegislationDocument document : documents) {
	                        if (document.getDocId() == possibleNewLegislationDocument.getDocId()) {
	                            if (Objects.equals(document.getTextHash(), possibleNewLegislationDocument.getTextHash())) break;
	                            try {
	                            	possibleNewLegislationDocument = getDocContent(possibleNewLegislationDocument); // It's new, so we'll fetch the actual doc content
								} catch (UnirestException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                            effectiveLegislation.documents.remove(document);
	                            effectiveLegislation.documents.add(possibleNewLegislationDocument);
	                            updateDocument(possibleNewLegislationDocument); // Save it to our repository
	                            replacedDoc = true;
	                            break;
	                        }
	                    }
	
	                    if (!replacedDoc) effectiveLegislation.documents.add(possibleNewLegislationDocument);
                    }

                });
                
                List<Representative> billSponsors = effectiveLegislation.getSponsors();
                if (billSponsors != null) {
            		log.info("Legislation {} already has sponsors... {}", effectiveLegislation.getBill_id(), billSponsors.size());
                	if (incomingLegislation.getChange_hash() != legislation.getChange_hash()) { // Incoming Legislation is new AND already has sponsors
                		log.info("Adding stubs for {}", effectiveLegislation.getBill_id());
                		billSponsors.forEach(sponsor -> { // Add stubs from the db legislation
	                        representativeRepository.saveIfDoesNotExist(sponsor.toEntity());
                		});
                    }
	                sponsorsArray.forEach(sponsor -> {
	                    int peopleId = sponsor.getAsJsonObject().get("people_id").getAsInt();
	                    boolean exists = billSponsors.stream().anyMatch(rep -> rep.getPeople_id() == peopleId);
	                    if (!exists) { // This sponsor wasn't on the bill before
		                    log.info("Representative {} does not exist in the DB already!", peopleId);
	                        Representative representativeToAdd = RepresentativeService.fillRecord(sponsor.getAsJsonObject());
	                        billSponsors.add(representativeToAdd);
	                        updateRepresentative(representativeToAdd);
	                    }
	                });
                } else {
                	
                	log.warn("Bill {} getSponsors was empty", effectiveLegislation.getBill_id());
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

    /**
     * For a piece of {@link Legislation}, get the documents
     * @param legislationDocument
     * @return The original input with the documents added
     * @apiNote LegiScan called.
     * @throws UnirestException
     */
    protected LegislationDocument getDocContent(LegislationDocument legislationDocument) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getBillText")
                .queryString("id", legislationDocument.getDocId())
                .asJson();

        if (response.getStatus() == 200) {
            JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
            Utils.checkLegiscanResponseStatus(jsonObject);

            JsonObject textObject = jsonObject.get("text").getAsJsonObject();

            return LegislationDocument.fillDocument(textObject);

        }
        log.error("Failed to fetch bill text");
        return legislationDocument;
    }

    public Legislation getLegislationById(int legislationId) {
        return legislationRepository.getReferenceById(legislationId).toModel();
    }

    /**
     * For a piece of {@link Legislation}, get all of them by state (paginated)
     * @param state abbreviation
     * @param page page number
     * @param size number of legislation per page
     * @return a page of legislation objects
     * @apiNote LegiScan called.
     */
    public Page<Legislation> getLegislationByState(StateEnum state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LegislationEntity> legislationEntities = legislationRepository.findByStateOrderByStatusDateDesc(state, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }

    /**
     * For a piece of {@link Legislation}, get all of them by state (paginated)
     * @param stateId
     * @param page page number
     * @param size number of legislation per page
     * @return a list of legislation objects
     */
    public Page<Legislation> getLegislationByStateId(int stateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        StateEnum state = StateEnum.fromStateID(stateId);
        Page<LegislationEntity> legislationEntities = legislationRepository.findByStateOrderByStatusDateDesc(state, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }

    /**
     * Get all {@link Legislation} for who a representative is a sponsor (paginated)
     * @param repId rep id
     * @param page page number
     * @param size number of legislation per page
     * @return a list of legislation objects
     */
    public Page<Legislation> getLegislationByRepresentativeIdPaginated(int repId, int page, int size) {
        RepresentativeEntity representative = representativeRepository.findById(repId)
                .orElseThrow(() -> new EntityNotFoundException("Representative not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<LegislationEntity> legislationEntities = legislationRepository.findBySponsorsOrderByStatusDateDesc(representative, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }
    
    /**
     * Get all {@link Legislation} for who a representative is a sponsor
     * @param repId rep id
     * @return a list of legislation objects
     */
    public List<Legislation> getLegislationByRepresentativeId(int repId) {
        RepresentativeEntity representative = representativeRepository.findById(repId)
                .orElseThrow(() -> new EntityNotFoundException("Representative not found"));

        List<LegislationEntity> legislationEntities = legislationRepository.findBySponsorsOrderByStatusDateDesc(representative);
        return legislationEntities.stream()
                .map(LegislationEntity::toModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all {@link Legislation} by its session id (paginated)
     * @param sessionId sessionId id
     * @param page page number
     * @param size number of legislation per page
     * @return a list of legislation objects
     */
    public Page<Legislation> getLegislationBySessionIdPaginated(int sessionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LegislationEntity> legislationEntities = legislationRepository.findBySessionId(sessionId, pageable);
        return legislationEntities.map(LegislationEntity::toModel);
    }

    
    /**
     * From a list of roll calls, add or update the DB entries
     * @param rollCalls
     */
    private void updateRollCalls(List<RollCall> rollCalls) {
    	if (rollCalls.isEmpty()) return;
    	List<RollCallEntity> rollCallEntities = new ArrayList<RollCallEntity>();
    	for (RollCall rollCall : rollCalls) {
    		rollCallEntities.add(rollCall.toEntity());
    	}
    	rollCallRepository.saveAllAndFlush(rollCallEntities);
    }
    
    /**
     * Update the legislation document in our db
     * @param document
     */
    private void updateDocument(LegislationDocument document) {
    	legislationDocumentRepository.saveAndFlush(document.toEntity());
    }
    
    /**
     * From the provided {@link Representative}, fetch our DB one and update it if the incoming is different.
     * @param rep {@link Representative} to insert/update
     */
    private void updateRepresentative(Representative rep) {
    	Optional<RepresentativeEntity> representativeDB = representativeRepository.findById(rep.getPeople_id());
    	
    	if (representativeDB.isPresent() && representativeDB.get().getPerson_hash().equals(rep.getPerson_hash())) {	// Nothing has changed.
			return;
		}
    	
    	log.info("Saving rep {} with person_hash {} to the db", rep.getPeople_id(), rep.getPerson_hash());
		representativeRepository.saveAndFlush(rep.toEntity());
		return;
    }

}
