package com.backend.legisloop.controller;

import com.backend.legisloop.model.AppUser;
import com.backend.legisloop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppUserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<AppUser> getUsers() {
        return (List<AppUser>) userRepository.findAll();
    }

    @PostMapping("/users")
    void addUser(@RequestBody AppUser appUser) {
        userRepository.save(appUser);
    }
}

