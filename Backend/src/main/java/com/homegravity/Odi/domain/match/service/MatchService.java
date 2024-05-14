package com.homegravity.Odi.domain.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homegravity.Odi.domain.match.dto.MatchRequestDTO;
import com.homegravity.Odi.domain.match.dto.MatchResponseDTO;
import com.homegravity.Odi.domain.match.repository.MatchRepository;
import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final MatchRepository matchRepository;

    public synchronized MatchResponseDTO createMatch(MatchRequestDTO matchRequestDto, Long memberId) throws JsonProcessingException {

        // 요청자 유효성 검사
        Member requestMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "없는 사용자입니다."));

        String memberKey = "member:" + memberId;
        Boolean alreadyRequested = redisTemplate.opsForValue().setIfAbsent(memberKey, "active", 1, TimeUnit.HOURS);

        // 중복 매칭 검사
        if (Boolean.FALSE.equals(alreadyRequested)) {
            log.info("이미 활성화된 요청이 있습니다: {}", memberId);
            throw new BusinessException(ErrorCode.MATCH_ALREADY_EXIST, ErrorCode.MATCH_ALREADY_EXIST.getMessage());
        }

        long sequence = getNextSequence("member");

        // 순서 저장
        matchRepository.addOrders(String.valueOf(memberId), String.valueOf(sequence));
//        String memberSequence = String.valueOf(memberId + "_" + String.valueOf(sequence));

        // 요청자의 출발지, 목적지 위치 저장
        matchRepository.addLocation("departures", matchRequestDto.getDepLat(), matchRequestDto.getDepLon(), String.valueOf(memberId));
        matchRepository.addLocation("arrivals", matchRequestDto.getArrLat(), matchRequestDto.getArrLon(), String.valueOf(memberId));

        // 사용자 요청 정보를 저장
        matchRepository.addRequest(String.valueOf(memberId), matchRequestDto);

        // 매칭
        return findNearbyTarget("departures", "arrivals", String.valueOf(memberId), matchRequestDto);

    }

    // 주어진 위치와 반경 내의 사용자를 매칭
    private MatchResponseDTO findNearbyTarget(String departuresKey, String arrivalsKey, String memberId, MatchRequestDTO matchRequestDTO) throws JsonProcessingException {

        // 출발지 반경 1km내의 사용자 조회
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

        // 도착지 반경 1km내의 사용자 조회
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

        // 자기 자신의 제거
        depResult.remove(memberId);
        arrResult.remove(memberId);
        // 두 결과의 교집합 계산
        depResult.retainAll(arrResult);

        if (depResult.isEmpty()) {
            log.info("현재 매칭 상대를 찾을 수 없습니다. 잠시 기다려주세요");
            return null;
        }

        // 가장 먼저 들어온 상대와 매칭
        String firstMember = depResult.stream().min((a, b) -> {
            Long seqA = matchRepository.getOrders(a);
            Long seqB = matchRepository.getOrders(b);
            return Long.compare(seqA, seqB);
        }).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, "매칭할 사용자가 없습니다."));

        log.info("가장 먼저 들어온 유저 {} 와 매칭", firstMember);

        // 요청 정보 조회
        MatchRequestDTO firstMemberRequest = matchRepository.getRequestByMemberId(firstMember);
        MatchRequestDTO memberRequest = matchRepository.getRequestByMemberId(memberId);

        // Redis에서 매칭된 사용자 정보 삭제
        matchRepository.removeMatch(firstMember);
        matchRepository.removeMatch(memberId);

        // 파티 생성
        Long partyId = partyService.createMatchParty(Long.parseLong(firstMember), Long.parseLong(memberId), firstMemberRequest, memberRequest);

        return MatchResponseDTO.of(Long.parseLong(firstMember), Long.parseLong(memberId), partyId);
    }

    // 사용자 요청 순서 정리
    private long getNextSequence(String keyPrefix) {
        return redisTemplate.opsForValue().increment(keyPrefix + ":sequence", 1);
    }

    // 매칭 취소
    public void cancelMatch(Long memberId) {

        // 유효성 검사
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));
        matchRepository.removeMatch(String.valueOf(memberId));

    }

}
