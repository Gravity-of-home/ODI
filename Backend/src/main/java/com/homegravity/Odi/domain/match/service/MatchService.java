package com.homegravity.Odi.domain.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.match.dto.MatchResponseDTO;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.party.service.PartyService;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PartyService partyService;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 사용자 요청 순서 정리
    public long getNextSequence(String keyPrefix) {
        return redisTemplate.opsForValue().increment(keyPrefix + ":sequence", 1);
    }

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

    // 주어진 위치와 반경 내의 사용자를 매칭
    public MatchResponseDTO findNearbyTarget(String departuresKey, String arrivalsKey, String memberSequence, String memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

        log.warn("[start] ============= findNearByTarget:: {}의 매칭 시작!! =============", memberId);

        // 출발지에서 근접한 사용자 검색 결과 저장
//        GeoResults<RedisGeoCommands.GeoLocation<String>> depResult = redisTemplate.opsForGeo()
//                .radius(departuresKey,
//                        new Circle(new Point(matchRequestDTO.getDepLon(), matchRequestDTO.getDepLat()), new Distance(2, Metrics.KILOMETERS)),
//                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
//                                .includeDistance()
//                                .includeCoordinates());

        Set<String> depResult = redisTemplate.opsForGeo()
                .radius(departuresKey,
                        new Circle(new Point(matchRequestDTO.getDepLon(), matchRequestDTO.getDepLat()), new Distance(1.0, Metrics.KILOMETERS)),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeDistance()
                                .includeCoordinates())
                .getContent()
                .stream()
                .map(GeoResult::getContent)
                .map(GeoLocation::getName)
                .collect(Collectors.toSet());

        // 목적지에서 근접한 사용자 검색 결과 저장
//        GeoResults<RedisGeoCommands.GeoLocation<String>> arrResult = redisTemplate.opsForGeo()
//                .radius(arrivalsKey,
//                        new Circle(new Point(matchRequestDTO.getArrLon(), matchRequestDTO.getArrLat()), new Distance(2, Metrics.KILOMETERS)),
//                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
//                                .includeDistance()
//                                .includeCoordinates());

        Set<String> arrResult = redisTemplate.opsForGeo()
                .radius(arrivalsKey,
                        new Circle(new Point(matchRequestDTO.getArrLon(), matchRequestDTO.getArrLat()), new Distance(1.0, Metrics.KILOMETERS)),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeDistance()
                                .includeCoordinates())
                .getContent()
                .stream()
                .map(GeoResult::getContent)
                .map(GeoLocation::getName)
                .collect(Collectors.toSet());

        log.warn("출발지 결과 {}", depResult.size());
        for (String tmp : depResult) {
            log.info("출발지 결과 하나하나 : {}", tmp);
        }

        log.warn("도착지 결과 {}", arrResult.size());
        for (String tmp : arrResult) {
            log.info("도착지 결과 하나하나 : {}", tmp);
        }

        // 자기 자신의 결과 제거
        depResult.remove(memberSequence);
        arrResult.remove(memberSequence);
        // 두 결과의 교집합 계산
        depResult.retainAll(arrResult);

        log.info("교집합 계산 했나요? : {}", depResult.size());

        // 디버깅 용 반복문
//        for (String memberInfo : depResult) {
//            log.info("{}랑 매칭해줄게? ", memberInfo);
//        }

        if(depResult.isEmpty()) {
            log.info("현재 매칭 상대를 찾을 수 없습니다. 재시도 하겠슴돠");
            return null;
        }

        // 가장 먼저 들어온 상대와 매칭
        String firstMember = depResult.stream().min((a, b) -> {
            int seqA = extractSequence(a);
            int seqB = extractSequence(b);
            return Integer.compare(seqA, seqB);
        }).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "매칭할 사용자가 없습니다."));

        String[] memberOrder = firstMember.split("_");
        log.info("가장 먼저 들어온 유저 {} 와 매칭", memberOrder[0]);

        // 요청 정보 조회
        MatchRequestDTO firstMemberRequest = getRequestByMemberId(memberOrder[0]);
        MatchRequestDTO memberRequest = getRequestByMemberId(memberId);

        // Redis에서 매칭된 사용자 정보 삭제
        removeMatch(firstMember);
        removeMatch(memberSequence);

        log.warn("[end] =========== findNearByTarget 종료 ================ ");

        // 파티 생성
        Long partyId = partyService.createMatchParty(Long.parseLong(memberOrder[0]), Long.parseLong(memberId), firstMemberRequest, memberRequest);

        return MatchResponseDTO.of(Long.parseLong(memberOrder[0]), Long.parseLong(memberId), partyId);

    }


    // 멤버 ID로 MatchRequestDTO 조회
    public MatchRequestDTO getRequestByMemberId(String memberId) throws JsonProcessingException {
        log.warn("[start] =========== matchRequest 가져오는 메서드 : {} ================ ", memberId);
        String matchRequestJson = (String) redisTemplate.opsForHash().get("match_requests", memberId);
        log.warn("[end] =========== matchRequest 가져오는 메서드 : {} ================ ", memberId);
        return objectMapper.readValue(matchRequestJson, MatchRequestDTO.class);

    }


    private int extractSequence(String memberInfo) {
        // memberInfo 형식: "memberId_sequence"
        String[] parts = memberInfo.split("_");
        return Integer.parseInt(parts[1]); // 시퀀스 번호 추출
    }

    public synchronized MatchResponseDTO createMatch(MatchRequestDTO matchRequestDto, Member member) throws JsonProcessingException {

        log.warn("[start] =========== enterMatch 시작 ================ ");

        String memberKey = "member:" + member.getId();
        Boolean alreadyRequested = redisTemplate.opsForValue().setIfAbsent(memberKey, "active", 1, TimeUnit.HOURS);

        if (Boolean.FALSE.equals(alreadyRequested)) {
            log.info("이미 활성화된 요청이 있습니다: {}", member.getId());
            throw new BusinessException(ErrorCode.MATCH_ALREADY_EXIST, ErrorCode.MATCH_ALREADY_EXIST.getMessage());
        }

        long sequence = getNextSequence("member");
        String memberSequence = String.valueOf(member.getId() + "_" + String.valueOf(sequence));

        addLocation("departures", matchRequestDto.getDepLat(), matchRequestDto.getDepLon(), memberSequence);
        addLocation("arrivals", matchRequestDto.getArrLat(), matchRequestDto.getArrLon(), memberSequence);

        // TODO : 요청 정보 저장
        // 사용자 요청 정보를 Redis에 추가
        addRequest(String.valueOf(member.getId()), matchRequestDto);

        // 매칭
        return findNearbyTarget("departures", "arrivals", memberSequence, String.valueOf(member.getId()), matchRequestDto);

    }


    // TODO: 매칭 요청 삭제
    public void removeMatch(String memberSequence) {

        log.warn("[start] =========== 매칭 정보 {} 삭제 시작 ================ ", memberSequence);

        String memberId = memberSequence.split("_")[0];

        // Redis에서 매칭 요청 정보 삭제
        redisTemplate.opsForHash().delete("match_requests", memberId);
        log.info("매칭 요청 정보 삭제: {}", memberId);

        // Redis에서 위치 정보 삭제
        String depKey = "departures";
        String arrKey = "arrivals";
        Long count1 = redisTemplate.opsForGeo().remove(depKey, memberSequence);
        log.info("출발지에서 {}의 위치 삭제 : {}", memberSequence, count1);

        Long count2 = redisTemplate.opsForGeo().remove(arrKey, memberSequence);
        log.info("도착지에서 {}의 위치 삭제 : {}", memberId, count2);

        // Redis에서 순서 정보 삭제
        String sequenceKey = "member_sequence";
        redisTemplate.delete(sequenceKey + ":" + memberId);
        log.info("순서 정보 삭제: {}", sequenceKey + ":" + memberId);

        log.warn("[end] =========== 매칭 정보 삭제 종료 ================ ");

    }
}
