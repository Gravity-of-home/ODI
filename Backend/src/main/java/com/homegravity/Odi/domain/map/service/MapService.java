package com.homegravity.Odi.domain.map.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homegravity.Odi.domain.map.dto.MapResponseDTO;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapService {

    private final WebClient naverWebClient;
    private final PartyRepository partyRepository;

    public String getNaverPathInfo(Double departuresX, Double departuresY, Double arrivalsX, Double arrivalsY) {

        String uriTemplate = "?start={startX},{startY}&goal={goalX},{goalY}";

        return naverWebClient.get()
                .uri(uriTemplate, departuresX, departuresY, arrivalsX, arrivalsY)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 처리
    }

    public Integer getPartyPathInfo(Long partyId) throws JsonProcessingException {

        Party party = partyRepository.findParty(partyId).orElseThrow(() -> new BusinessException(ErrorCode.PARTY_NOT_EXIST, ErrorCode.PARTY_NOT_EXIST.getMessage()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록 - LocalDateTime 사용 가능

        String uriTemplate = "?start={startX},{startY}&goal={goalX},{goalY}";

        String response = naverWebClient.get()
                .uri(uriTemplate, party.getDeparturesLocation().getX(), party.getDeparturesLocation().getY(), party.getArrivalsLocation().getX(), party.getArrivalsLocation().getY())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 처리

        MapResponseDTO result = objectMapper.readValue(response, MapResponseDTO.class);

        return result.getRoute().getTraoptimal().get(0).getSummary().getTaxiFare();

    }
}
