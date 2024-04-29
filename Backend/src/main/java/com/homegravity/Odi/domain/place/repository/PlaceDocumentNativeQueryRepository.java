package com.homegravity.Odi.domain.place.repository;

import com.homegravity.Odi.domain.place.entity.PlaceDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
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
}
