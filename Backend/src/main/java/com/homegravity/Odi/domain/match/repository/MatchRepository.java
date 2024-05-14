package com.homegravity.Odi.domain.match.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MatchRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    // 위치 정보를 Redis에 추가
    public void addLocation(String key, double lat, double lon, String memberInfo) {
        redisTemplate.opsForGeo().add(key, new Point(lon, lat), memberInfo);
    }

    // 사용자 요청 정보를 Redis에 추가
    public void addRequest(String memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {
        String matchRequestJson = objectMapper.writeValueAsString(matchRequestDTO);
        redisTemplate.opsForHash().put("match_requests", memberId, matchRequestJson);
    }

    // 멤버 ID로 MatchRequestDTO 조회
    public MatchRequestDTO getRequestByMemberId(String memberId) throws JsonProcessingException {
        String matchRequestJson = (String) redisTemplate.opsForHash().get("match_requests", memberId);
        return objectMapper.readValue(matchRequestJson, MatchRequestDTO.class);
    }

    // 매칭 요청 삭제
    public void removeMatch(String memberSequence) {

        String memberId = memberSequence.split("_")[0];

        // Redis에서 매칭 요청 정보 삭제
        redisTemplate.opsForHash().delete("match_requests", memberId);

        // Redis에서 위치 정보 삭제
        String depKey = "departures";
        String arrKey = "arrivals";
        redisTemplate.opsForGeo().remove(depKey, memberSequence);
        redisTemplate.opsForGeo().remove(arrKey, memberSequence);

        // Redis에서 순서 정보 삭제
        String sequenceKey = "member_sequence";
        redisTemplate.delete(sequenceKey + ":" + memberId);

    }

}
