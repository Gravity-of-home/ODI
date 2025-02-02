package com.homegravity.Odi.domain.place.service;

import com.homegravity.Odi.domain.place.dto.PlaceDocumentDto;
import com.homegravity.Odi.domain.place.dto.request.PlaceSearchDto;
import com.homegravity.Odi.domain.place.repository.PlaceDocumentNativeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceDocumentNativeQueryRepository placeDocumentNativeQueryRepository;

    // 장소명으로 장소 리스트 검색
    public Slice<PlaceDocumentDto> searchPlacesByPlaceName(PlaceSearchDto requestDto, Pageable pageable) {

        return new PageImpl<>(placeDocumentNativeQueryRepository
                .searchPlaces(requestDto.getQuery(), new GeoPoint(requestDto.getLatitude(), requestDto.getLongitude()), pageable)
                .stream().map(PlaceDocumentDto::from).toList());
    }

    // 가장 가까운 장소 조회
    public PlaceDocumentDto getNearestPlace(Double latitude, Double longitude) {

        return PlaceDocumentDto.from(placeDocumentNativeQueryRepository.getNearestPlace(new GeoPoint(latitude, longitude)));
    }

}
