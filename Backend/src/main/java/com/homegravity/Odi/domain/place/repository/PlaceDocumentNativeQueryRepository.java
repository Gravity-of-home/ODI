package com.homegravity.Odi.domain.place.repository;

import com.homegravity.Odi.domain.place.entity.PlaceDocument;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.GeoDistanceOrder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PlaceDocumentNativeQueryRepository {

    // 탐색할 최대 거리 값.
    private static final String PLACE_MAX_DISTANCE = "1km";

    private final ElasticsearchOperations elasticsearchOperations;

    // 장소명으로 장소 검색, 현재 위치에서 가까운 순으로 정렬
    public List<PlaceDocument> searchPlaces(String placeName, GeoPoint geoPoint, Pageable pageable) {

        Query query = NativeQuery.builder()
                .withQuery(
                        q -> q.bool(
                                b -> b.should(
                                        s -> s.multiMatch(
                                                m -> m.fields("place_name.analyzed").query(placeName)
                                        )
                                )
                        )
                )
                .withSort(Sort.by(
                        new GeoDistanceOrder("location-geopoint", geoPoint)
                ))
                .withPageable(pageable)
                .build();

        SearchHits<PlaceDocument> searchHits = elasticsearchOperations.search(query, PlaceDocument.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    // 가장 가까운 장소 조회
    public PlaceDocument getNearestPlace(GeoPoint geoPoint) {

        Query query = NativeQuery.builder()
                .withQuery(
                        q -> q.geoDistance(
                                g -> g.distance(PLACE_MAX_DISTANCE)
                                        .field("location-geopoint").
                                        location(loc -> loc.latlon(Queries.latLon(geoPoint)))
                        )
                )
                .withSort(Sort.by(
                        new GeoDistanceOrder("location-geopoint", geoPoint)
                ))
                .withMaxResults(1)
                .build();

        SearchHits<PlaceDocument> searchHits = elasticsearchOperations.search(query, PlaceDocument.class);

        if (searchHits.isEmpty()) {
            throw new BusinessException(ErrorCode.NEAR_PLACE_NOT_EXIST, "반경 " +PLACE_MAX_DISTANCE+"내에 있는 장소를 찾을 수 없습니다.");
        }

        return searchHits.getSearchHit(0).getContent();
    }
}
