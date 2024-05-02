package com.homegravity.Odi.domain.map.controller;

import com.homegravity.Odi.domain.map.service.MapService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
