package com.homegravity.Odi.domain.map.controller;

import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class MapController {

    private final WebClient naverWebClient;

    @GetMapping("/path-info")
    public ApiResponse<String> getPathInfo(@RequestParam("departuresX") String departuresX,
                                           @RequestParam("departuresY") String departuresY,
                                           @RequestParam("arrivalsX") String arrivalsX,
                                           @RequestParam("arrivalsY") String arrivalsY) {

        String uriTemplate = "?start={startX},{startY}&goal={goalX},{goalY}";

        String response = naverWebClient.get()
                .uri(uriTemplate, departuresX, departuresY, arrivalsX, arrivalsY)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 처리

        return ApiResponse.of(SuccessCode.PATH_INFO_GET_SUCCESS, response);
    }

}
