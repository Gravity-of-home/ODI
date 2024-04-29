package com.homegravity.Odi.domain.place.repository;

import com.homegravity.Odi.domain.place.entity.PlaceDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceDocumentRepository extends ElasticsearchRepository<PlaceDocument, Long> {

}
