package com.homegravity.Odi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@Tag(name = "z오디", description = "오디 API 호출 테스트")
public class OdiApplication {

    // 연결 테스트
    @Operation(summary = "Hello World", description = "연결 테스트 메서드")
    @GetMapping("/api/hello")
    String hello() {
        return "Hello World~";
    }

    public static void main(String[] args) {
        SpringApplication.run(OdiApplication.class, args);
    }

}
