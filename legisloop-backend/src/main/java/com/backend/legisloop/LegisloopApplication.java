package com.backend.legisloop;

import com.backend.legisloop.model.AppUser;
import com.backend.legisloop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.stream.Stream;

@SpringBootApplication
@EnableScheduling
public class LegisloopApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegisloopApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> Stream.of("Jono", "Anthony", "Steph", "Sean", "Damien").forEach(name -> {
            AppUser appUser = new AppUser(name, name.toLowerCase() + "@domain.com");
            userRepository.save(appUser);
        });
	}
}
