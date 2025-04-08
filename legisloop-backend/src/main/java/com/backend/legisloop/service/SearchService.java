package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;


import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.PolicyAreasEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Legislation> searchLegislation(String term, int page, int size) {

        SearchSession searchSession = Search.session(entityManager);

        return searchSession.search(LegislationEntity.class)
                .where(f -> f.match()
                        .fields("title", "description", "summary")
                        .matching(term))
                .fetchHits(page * size, size)
                .stream()
                .map(LegislationEntity::toModel)
                .toList();
    }

    public List<Representative> searchRepresentatives(String term, int page, int size) {
        SearchSession searchSession = Search.session(entityManager);

       return searchSession.search(RepresentativeEntity.class)
                .where(f -> f.match()
                        .fields("name")
                        .matching(term))
                .fetchHits(page * size, size)
                .stream()
                .map(RepresentativeEntity::toModel)
                .toList();
    }

    public List<Legislation> searchLegislationKeywords(PolicyAreasEnum policyArea, int page, int size) {
        SearchSession searchSession = Search.session(entityManager);

        String keyword = policyArea.getPolicyArea();
        return searchSession.search(LegislationEntity.class)
                .where(f -> {
                    BooleanPredicateClausesStep<?> boolBuilder = f.bool();
                    boolBuilder.should(f.match().fields("title", "description", "summary").matching(keyword));
                    return boolBuilder;
                })
                .fetchHits(page * size, size)
                .stream()
                .map(LegislationEntity::toModel)
                .toList();
    }

    public String initializeHibernateSearch() {
        try {
            Search.session(entityManager)
                    .massIndexer()
                    .startAndWait();
            return "Initialized indexes successfully";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Mass indexing was interrupted", e);
        }
    }
}
