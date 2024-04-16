package com.homegravity.Odi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJpaAuditing
@SpringBootApplication
public class OdiApplication {

	// 연결 테스트
	@GetMapping("/api/hello")
	String hello() {
		return "Hello World~";
	}


	public static void main(String[] args) {
		SpringApplication.run(OdiApplication.class, args);
	}

}
