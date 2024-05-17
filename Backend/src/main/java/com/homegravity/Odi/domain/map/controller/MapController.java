package com.homegravity.Odi.domain.map.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homegravity.Odi.domain.map.service.MapService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/path-info")
    public ApiResponse<String> getPathInfo(@RequestParam("departuresX") Double departuresX,
                                           @RequestParam("departuresY") Double departuresY,
                                           @RequestParam("arrivalsX") Double arrivalsX,
                                           @RequestParam("arrivalsY") Double arrivalsY) {

        return ApiResponse.of(SuccessCode.PATH_INFO_GET_SUCCESS, mapService.getNaverPathInfo(departuresX, departuresY, arrivalsX, arrivalsY));
    }


    @GetMapping("/{party-id}/taxi-fare")
    public ApiResponse<Integer> getTaxiFare(@PathVariable("party-id") Long partyId) throws JsonProcessingException {
        return ApiResponse.of(SuccessCode.PATH_INFO_GET_SUCCESS, mapService.getPartyPathInfo(partyId));
    }

}
