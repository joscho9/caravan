package com.example.caravan_spring_project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaravanSpringProjectApplication {

	private static final Logger log = LoggerFactory.getLogger(CaravanSpringProjectApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CaravanSpringProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(){
		return args -> {
			log.info("Eine selbst geloggte Nachricht");
			log.warn("Eine selbst erstellte Warnung");
			log.error("Ein selbst erstellter Error");
		};
	}

}
