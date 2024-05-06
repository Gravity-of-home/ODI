package com.homegravity.Odi.domain.party.respository;

import com.homegravity.Odi.domain.party.entity.PartyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PartyDocumentRepository extends ElasticsearchRepository<PartyDocument, Long> {
}
