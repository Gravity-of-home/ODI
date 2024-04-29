package com.homegravity.Odi.domain.place.service;

import com.homegravity.Odi.domain.place.dto.PlaceDocumentDto;
import com.homegravity.Odi.domain.place.dto.request.PlaceSearchDto;
import com.homegravity.Odi.domain.place.repository.PlaceDocumentNativeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceDocumentNativeQueryRepository placeDocumentNativeQueryRepository;

    // 장소명으로 장소 리스트 검색
    public List<PlaceDocumentDto> searchPlacesByPlaceName(PlaceSearchDto requestDto) {

        return placeDocumentNativeQueryRepository
                .searchPlaces(requestDto.getPlaceName(), requestDto.getLatitude(), requestDto.getLongitude())
                .stream().map(PlaceDocumentDto::from).toList();
    }


}
