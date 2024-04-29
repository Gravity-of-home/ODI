package com.homegravity.Odi.domain.place.controller;

import com.homegravity.Odi.domain.place.dto.PlaceDocumentDto;
import com.homegravity.Odi.domain.place.dto.request.PlaceSearchDto;
import com.homegravity.Odi.domain.place.entity.PlaceDocument;
import com.homegravity.Odi.domain.place.service.PlaceService;
import com.homegravity.Odi.global.response.success.ApiResponse;
import com.homegravity.Odi.global.response.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="장소 검색", description = "장소명/주소/위치 기준 검색")
@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "장소명 검색", description = "장소명으로 장소를 검색합니다. 거리가 가까운 순으로 정렬하여 결과를 제공합니다. ❗(장소명 검색 + 주소 검색 같이 할 수 있도록 수정예정)")
    @PostMapping()
    public ApiResponse<List<PlaceDocumentDto>> getPlaces(@RequestBody PlaceSearchDto requestDto) {
        return ApiResponse.of(SuccessCode.PLACE_LIST_GET_SUCCESS, placeService.searchPlacesByPlaceName(requestDto));
    }
}
