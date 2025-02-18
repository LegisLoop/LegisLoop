package com.backend.legisloop.repository;

import com.backend.legisloop.entities.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Integer> {
}
