package com.homegravity.Odi.domain.map.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MapService {

    private final WebClient naverWebClient;

    public String getNaverPathInfo(Double departuresX, Double departuresY, Double arrivalsX, Double arrivalsY) {
        String uriTemplate = "?start={startX},{startY}&goal={goalX},{goalY}";

        return naverWebClient.get()
                .uri(uriTemplate, departuresX, departuresY, arrivalsX, arrivalsY)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 처리
    }

}
