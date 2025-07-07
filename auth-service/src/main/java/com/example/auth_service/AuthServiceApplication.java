package com.example.auth_service;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = {
	    "com.example.auth_service",
	    "com.example.common.exception" 
	})
@EnableJpaRepositories("com.example.auth_service.repository")
@EntityScan("com.example.auth_service.model")
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner pwRunner() {
	  return args -> {
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String hash = encoder.encode("password");
	    System.out.println("USAR ESTE HASH: " + hash);
	  };
	}

}
