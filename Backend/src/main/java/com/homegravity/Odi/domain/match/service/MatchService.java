package com.homegravity.Odi.domain.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PartyService partyService;
    private final ObjectMapper objectMapper;

    public long getNextSequence(String keyPrefix) {
        return redisTemplate.opsForValue().increment(keyPrefix + ":sequence", 1);
    }

    // 위치 정보를 Redis에 추가하는 메서드
    // 매칭을 요청한 사용자들의 위치 정보를 저장?
    public void addLocation(String key, double lat, double lon, String memberInfo) {

//         중복 처리 방지
        // 위치 정보를 조회합니다.
//        List<Point> existingPoints = redisTemplate.opsForGeo().position(key, memberInfo);
//
//        // 이미 저장된 위치 정보가 있는 경우, 삭제하거나 업데이트 로직을 수행합니다.
//        if (existingPoints != null && !existingPoints.isEmpty()) {
//            // 기존 위치 정보 삭제
//            redisTemplate.opsForGeo().remove(key, memberInfo);
//            log.info("기존 위치 정보 삭제 : {}", memberInfo);
//        }

        // 저장
        log.warn("[start] =========== 위치 저장 시작 ================ ");
        redisTemplate.opsForGeo().add(key, new Point(lon, lat), memberInfo);
        log.warn("[end] =========== 위치 저장 종료 ================ ");

    }

    // 사용자 요청 정보를 Redis에 추가하는 메서드
    public void addRequest(String memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {
        log.warn("[start] =========== addRequest:: 요청 정보 저장 시작 ================ ");
        String matchRequestJson = objectMapper.writeValueAsString(matchRequestDTO);
        redisTemplate.opsForHash().put("match_requests", memberId, matchRequestJson);
        log.warn("[end] =========== addRequest:: 요청 정보 저장 종료 ================ ");
    }

    // 주어진 위치와 반경 내의 사용자를 찾는 메서드
    public Long findNearbyTarget(String departuresKey, String arrivalsKey, String memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

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
                        new Circle(new Point(matchRequestDTO.getDepLon(), matchRequestDTO.getDepLat()), new Distance(1, Metrics.KILOMETERS)),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeDistance()
                                .includeCoordinates())
                .getContent()
                .stream()
                .map(GeoResult::getContent)
                .map(GeoLocation::getName)
                .collect(Collectors.toSet());

        log.warn("출발지 결과 {}", depResult.size());

        // 목적지에서 근접한 사용자 검색 결과 저장
//        GeoResults<RedisGeoCommands.GeoLocation<String>> arrResult = redisTemplate.opsForGeo()
//                .radius(arrivalsKey,
//                        new Circle(new Point(matchRequestDTO.getArrLon(), matchRequestDTO.getArrLat()), new Distance(2, Metrics.KILOMETERS)),
//                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
//                                .includeDistance()
//                                .includeCoordinates());

        Set<String> arrResult = redisTemplate.opsForGeo()
                .radius(arrivalsKey,
                        new Circle(new Point(matchRequestDTO.getArrLon(), matchRequestDTO.getArrLat()), new Distance(1, Metrics.KILOMETERS)),
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


        // 두 결과의 교집합 계산
        depResult.retainAll(arrResult);
        log.info("교집합 계산 했나요? : {}", depResult.size());


        for (String memberInfo : depResult) {
            log.info("{}랑 매칭해줄게? ", memberInfo);
        }

        // 가장 먼저 들어온 상대와 매칭
        String firstMember = depResult.stream().min((a, b) -> {
            int seqA = extractSequence(a);
            int seqB = extractSequence(b);
            return Integer.compare(seqA, seqB);
        }).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "매칭할 사용자가 없습니다."));

        log.info("가장 먼저 들어온 유저 {} 와 매칭", firstMember);

        // TODO: 파티 생성
        MatchRequestDTO firstMemberRequest = getRequestByMemberId(firstMember);
        MatchRequestDTO memberRequest = getRequestByMemberId(memberId);

        // TODO: Redis에서 정보 삭제
        cancelMatch(memberId);

        log.warn("[end] =========== findNearByTarget 종료 ================ ");


        return partyService.createMatchParty(Long.parseLong(firstMember), Long.parseLong(memberId), firstMemberRequest, memberRequest);

    }


    // 멤버 ID로 MatchRequestDTO 가져오는 메서드
    public MatchRequestDTO getRequestByMemberId(String memberId) throws JsonProcessingException {
        String matchRequestJson = (String) redisTemplate.opsForHash().get("match_requests", memberId);
        return objectMapper.readValue(matchRequestJson, MatchRequestDTO.class);
    }

    private int extractSequence(String memberInfo) {
        // memberInfo 형식: "memberId_sequence"
        String[] parts = memberInfo.split("_");
        return Integer.parseInt(parts[1]); // 시퀀스 번호 추출
    }

    public synchronized void enterMatch(MatchRequestDTO matchRequestDto, Member member) throws JsonProcessingException {

        log.warn("[start] =========== enterMatch 시작 ================ ");

        String memberKey = "member:" + member.getId();
        Boolean alreadyRequested = redisTemplate.opsForValue().setIfAbsent(memberKey, "active", 1, TimeUnit.HOURS);

        if (Boolean.FALSE.equals(alreadyRequested)) {
            log.info("이미 활성화된 요청이 있습니다: {}", member.getId());
            return; // 이미 요청이 활성화되어 있다면 추가 요청을 무시합니다.
        }

        long sequence = getNextSequence("member");
        String depMemberInfo = String.valueOf(member.getId() + "_" + String.valueOf(sequence));
        String arrMemberInfo = String.valueOf(member.getId() + "_" + String.valueOf(sequence));

        addLocation("departures", matchRequestDto.getDepLat(), matchRequestDto.getDepLon(), depMemberInfo);
        addLocation("arrivals", matchRequestDto.getArrLat(), matchRequestDto.getArrLon(), arrMemberInfo);

        // TODO : 요청 정보 저장
        // 사용자 요청 정보를 Redis에 추가
        addRequest(String.valueOf(member.getId()), matchRequestDto);

        // 매칭
        findNearbyTarget("departures", "arrivals", String.valueOf(member.getId()), matchRequestDto);

        log.warn("[end] =========== enterMatch 종료 ================ ");


    }


    // TODO: 매칭 요청 삭제
    public void cancelMatch(String memberId) {
//        // 위치 정보를 조회합니다.
//        List<Point> existingPoints = redisTemplate.opsForGeo().position(key, memberInfo);
//
//        // 이미 저장된 위치 정보가 있는 경우, 삭제하거나 업데이트 로직을 수행합니다.
//        if (existingPoints != null && !existingPoints.isEmpty()) {
//            // 기존 위치 정보 삭제 
//            redisTemplate.opsForGeo().remove(key, memberInfo);
//            log.info("기존 위치 정보 삭제 : {}", memberInfo);
//        }

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

    // 위치 정보를 Redis에서 삭제하는 메소드
//    private void removeLocation(String key, String memberId) {
//
//        // memberId를 기반으로 실제 위치 정보 키를 생성하는 로직 필요
//        // 예시: memberId를 포함하는 모든 위치 정보 삭제
//        Set<String> keysToRemove = redisTemplate.opsForSet().members(key + ":" + memberId);
//        if (keysToRemove != null && !keysToRemove.isEmpty()) {
//            redisTemplate.opsForGeo().remove(key, String.valueOf(keysToRemove));
//            log.info("위치 정보 삭제: {} -> {}", key, keysToRemove);
//        }
//    }

    //    // 매칭된 사용자에게 매칭 제안하고 수락 여부 확인 후 파티 형성
//    public boolean createPartyIfAccepted(String userId, Set<String> matchedUsers) {
//        // 매칭 제안 로직 (실제 구현 필요)
//        // 이 예시에서는 모든 매칭이 수락된다고 가정
//        if (matchedUsers.contains(userId)) {
//            // 매칭 정보를 다른 데이터 구조에 저장
//            savePartyDetails(userId, matchedUsers);
//            return true;
//        }
//        return false;
//    }
//
//    // 파티 정보 저장 (상세 구현 필요)
//    private void savePartyDetails(String userId, Set<String> matchedUsers) {
//        // 파티 정보 저장 로직
//        // 예: Redis or any other database
//        System.out.println("Party created for user " + userId + " with users: " + matchedUsers);
//    }
}
