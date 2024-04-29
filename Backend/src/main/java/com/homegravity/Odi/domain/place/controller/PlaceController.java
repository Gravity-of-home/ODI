package com.homegravity.Odi.domain.place.controller;

import com.homegravity.Odi.domain.party.dto.LocationPoint;
import com.homegravity.Odi.domain.place.dto.PlaceDocumentDto;
import com.homegravity.Odi.domain.place.dto.request.PlaceSearchDto;
import com.homegravity.Odi.domain.place.service.PlaceService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "장소 검색", description = "장소명/주소/위치 기준 검색")
@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "장소명 검색", description = "장소명으로 장소를 검색합니다. 거리가 가까운 순으로 정렬하여 결과를 제공합니다. ❗(장소명 검색 + 주소 검색 같이 할 수 있도록 수정예정)")
    @GetMapping()
    public ApiResponse<Page<PlaceDocumentDto>> getPlaces(PlaceSearchDto requestDto, Pageable pageable) {
        return ApiResponse.of(SuccessCode.PLACE_LIST_GET_SUCCESS, placeService.searchPlacesByPlaceName(requestDto, pageable));
    }

    @Operation(summary = "가장 가까운 장소 조회", description = "위경도 기반으로 가장 가까운 장소를 조회합니다.")
    @GetMapping("/place")
    public ApiResponse<PlaceDocumentDto> getPlaces(LocationPoint geopoint) {
        return ApiResponse.of(SuccessCode.PLACE_LIST_GET_SUCCESS, placeService.getNearestPlace(geopoint.getLatitude(), geopoint.getLongitude()));
    }
}
