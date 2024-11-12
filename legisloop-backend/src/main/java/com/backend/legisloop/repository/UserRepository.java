package com.backend.legisloop.repository;

import com.backend.legisloop.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {
}
