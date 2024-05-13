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

        log.warn("[start] =========== 위치 저장 시작 ================ ");
        redisTemplate.opsForGeo().add(key, new Point(lon, lat), memberInfo);
        log.warn("[end] =========== 위치 저장 종료 ================ ");

    }

    // 사용자 요청 정보를 Redis에 추가
    public void addRequest(String memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {
        log.warn("[start] =========== addRequest:: {}의 요청 정보 저장 시작 ================ ", memberId);

        log.info("??? : {}", matchRequestDTO.toString());

        String matchRequestJson = objectMapper.writeValueAsString(matchRequestDTO);
        redisTemplate.opsForHash().put("match_requests", memberId, matchRequestJson);
        log.warn("[end] =========== addRequest:: 요청 정보 저장 종료 ================ ");
    }

    // 멤버 ID로 MatchRequestDTO 조회
    public MatchRequestDTO getRequestByMemberId(String memberId) throws JsonProcessingException {
        log.warn("[start] =========== matchRequest 가져오는 메서드 : {} ================ ", memberId);
        String matchRequestJson = (String) redisTemplate.opsForHash().get("match_requests", memberId);
        log.warn("[end] =========== matchRequest 가져오는 메서드 : {} ================ ", memberId);
        return objectMapper.readValue(matchRequestJson, MatchRequestDTO.class);

    }

    // 매칭 요청 삭제
    public void removeMatch(String memberId) {

        log.warn("[start] =========== 매칭 정보 삭제 시작 ================ ");

        // Redis에서 매칭 요청 정보 삭제
        redisTemplate.opsForHash().delete("match_requests", memberId);
        log.info("매칭 요청 정보 삭제: {}", memberId);

        // Redis에서 위치 정보 삭제
        String depKey = "departures";
        String arrKey = "arrivals";
        Long count1 = redisTemplate.opsForGeo().remove(depKey, memberId);
        log.info("출발지에서 {}의 위치 삭제 : {}", memberId, count1);

        Long count2 = redisTemplate.opsForGeo().remove(arrKey, memberId);
        log.info("도착지에서 {}의 위치 삭제 : {}", memberId, count2);

        // Redis에서 순서 정보 삭제
        String sequenceKey = "member_sequence";
        redisTemplate.delete(sequenceKey + ":" + memberId);
        log.info("순서 정보 삭제: {}", sequenceKey + ":" + memberId);

        log.warn("[end] =========== 매칭 정보 삭제 종료 ================ ");

    }

}
