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

    // 요청 순서를 Redis에 추가
    public void addOrders(String memberId, String sequence) {
        redisTemplate.opsForHash().put("match_orders", memberId, sequence);
    }

    // 위치 정보를 Redis에 추가
    public void addLocation(String key, double lat, double lon, String memberId) {
        redisTemplate.opsForGeo().add(key, new Point(lon, lat), memberId);
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

    public Long getOrders(String memberId) {
        return (Long) redisTemplate.opsForHash().get("match_orders", memberId);
    }

    // 매칭 요청 삭제
    public void removeMatch(String memberId) {

        // Redis에서 매칭 요청 정보 삭제
        Long a = redisTemplate.opsForHash().delete("match_requests", memberId);


        // Redis에서 위치 정보 삭제
        String depKey = "departures";
        String arrKey = "arrivals";
        Long b = redisTemplate.opsForGeo().remove(depKey, memberId);
        Long c = redisTemplate.opsForGeo().remove(arrKey, memberId);

        // Redis에서 순서 정보 삭제
        Boolean d = redisTemplate.delete("member:" + memberId);

        // Redis에서 요청 순서 정보 삭제
        Long e = redisTemplate.opsForHash().delete("match_orders", memberId);

        log.info("삭제 로직 확인 === dto 삭제 : {}, 위치정보 삭제 {}, {}, 시퀀스 삭제: {}, 순서 정보 삭제: {}", a, b, c, d, e);

    }

}
