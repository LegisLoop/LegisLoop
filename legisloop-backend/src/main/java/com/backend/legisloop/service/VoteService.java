package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.repository.VoteRepository;
import com.backend.legisloop.entities.VoteEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;
    
    private final VoteRepository voteRepository;
    private final RepresentativeRepository representativeRepository;

    /**
     * Get a page of votes that a representative has made
     * @param repId the legiscan person_id
     * @param page The page number to get
     * @param size The number of results
     * @return A page of Votes
     */
    public Page<Vote> getVotesByRepresentativeIdPaginated(int repId, int page, int size) {
        RepresentativeEntity representative = representativeRepository.findById(repId)
                .orElseThrow(() -> new EntityNotFoundException("Representative not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<VoteEntity> voteEntities = voteRepository.findByRepresentativeOrderByStatusDateDesc(representative, pageable);
        return voteEntities.map(VoteEntity::toModel);
    }
    
    /**
     * Get a page of votes that a representative has made
     * @param repId the legiscan person_id
     * @return A list of Votes
     */
    public List<Vote> getVotesByRepresentativeId(int repId) {
        RepresentativeEntity representative = representativeRepository.findById(repId)
                .orElseThrow(() -> new EntityNotFoundException("Representative not found"));

        List<VoteEntity> voteEntities = voteRepository.findByRepresentativeOrderByStatusDateDesc(representative);
        return voteEntities.stream()
                .map(VoteEntity::toModel)
                .collect(Collectors.toList());
    }
}
