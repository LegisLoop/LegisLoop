package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.entities.VoteEntity;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<LegislationEntity, Integer> {

	@Query(
			value = """
			SELECT l.bill_id          AS billId,
			       l.status_date      AS eventDate,
			       l.title            AS title,
			       l.description 	  AS description,
			       'SPONSORED'        AS type,
			       NULL               AS votePosition,
				   l.bill_id          AS tieBreaker
			FROM legislation_sponsors s
			JOIN legislation l ON l.bill_id = s.bill_id
			WHERE s.people_id = :personId

			UNION ALL

			SELECT rc.bill_id       AS billId,
			       rc.date                  AS eventDate,
			       lg.title                 AS title,
			       rc.description           AS description,
			       'VOTE'                   AS type,
			       v.vote_position    AS votePosition,
			       rc.roll_call_id    AS tieBreaker
			FROM votes v
			JOIN roll_calls rc ON v.roll_call_id = rc.roll_call_id
			JOIN legislation lg ON lg.bill_id = rc.bill_id
			WHERE v.representative_id = :personId

			ORDER BY eventDate DESC, tieBreaker DESC
			/*:pageable*/""",
			
			countQuery = """
			SELECT COUNT(*) FROM (
			  SELECT 1
			  FROM legislation_sponsors s
			  WHERE s.people_id = :personId
			  UNION ALL
			  SELECT 1
			  FROM votes v
			  WHERE v.representative_id = :personId
			) tmp""",
			nativeQuery = true
			)
	Page<EventProjection> findAllEventsByPerson(@Param("personId") int personId, Pageable pageable);
	
    @Query(
    	      value = """
    	         SELECT
    	           l.bill_id          AS billId,
    	           l.status_date      AS eventDate,
    	           l.title            AS title,
    	           l.description      AS description,
    	           'SPONSORED'        AS type,
    	           NULL               AS votePosition,
    	           l.bill_id          AS tieBreaker
    	         FROM legislation_sponsors s
    	         JOIN legislation l   ON l.bill_id = s.bill_id
    	         WHERE s.people_id = :personId
    	           AND (CAST(:startDate AS DATE) IS NULL OR l.status_date >= :startDate)
    	           AND (CAST(:endDate   AS DATE) IS NULL OR l.status_date <= :endDate)

    	         UNION ALL

    	         SELECT
    	           rc.bill_id  AS billId,
    	           rc.date            AS eventDate,
    	           lg.title           AS title,
    	           rc.description     AS description,
    	           'VOTE'             AS type,
    	           v.vote_position    AS votePosition,
    	           rc.roll_call_id    AS tieBreaker
    	         FROM votes v
    	         JOIN roll_calls rc  ON v.roll_call_id = rc.roll_call_id
    	         JOIN legislation lg ON lg.bill_id = rc.bill_id
    	         WHERE v.representative_id = :personId
    	           AND (CAST(:startDate AS DATE) IS NULL OR rc.date >= :startDate)
    	           AND (CAST(:endDate   AS DATE) IS NULL OR rc.date <= :endDate)

    	         ORDER BY eventDate DESC, tieBreaker DESC
    	      """,
    	      nativeQuery = true
    	    )
    List<EventProjection> findAllEventsByPersonAndDateRange(
    	      @Param("personId")  int    personId,
    	      @Param("startDate") Date   startDate,
    	      @Param("endDate")   Date   endDate
    	    );

	public interface EventProjection {
		Integer getBillId();

		Date getEventDate();

		String getTitle();

		String getDescription();

		String getType();

		Integer getVotePosition(); // nullable for sponsored events
	}

}
