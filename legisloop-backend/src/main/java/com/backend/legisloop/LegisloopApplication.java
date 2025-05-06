package com.backend.legisloop;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@Slf4j
public class LegisloopApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegisloopApplication.class, args);
	}
}
