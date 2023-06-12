package com.dodoDev.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MyPetSosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyPetSosApiApplication.class, args);
	}
}


