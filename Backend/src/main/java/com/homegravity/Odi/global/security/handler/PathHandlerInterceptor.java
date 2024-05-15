package com.homegravity.Odi.global.security.handler;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.domain.party.entity.Party;
import com.homegravity.Odi.domain.party.entity.PartyMember;
import com.homegravity.Odi.domain.party.respository.PartyMemberRepository;
import com.homegravity.Odi.domain.party.respository.PartyRepository;
import com.homegravity.Odi.global.config.PathPropertiesConfig;
import com.homegravity.Odi.global.jwt.filter.JWTFilter;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class PathHandlerInterceptor implements HandlerInterceptor {

    @Value("${PATH}")
    private String[] pathUrl;

    private final PathPropertiesConfig pathPropertiesConfig;

    private final PartyRepository partyRepository;

    private final MemberRepository memberRepository;

    private final PartyMemberRepository partyMemberRepository;

    private final JWTUtil jwtUtil;
    private final JWTFilter jwtFilter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();

        Long myId = Long.parseLong(jwtUtil.getId(jwtFilter.parseBearerToken(request.getHeader(HttpHeaders.AUTHORIZATION))));

        Member me = memberRepository.findByIdAndDeletedAtIsNull(myId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));

        for (String path : pathUrl) {
            Pattern pattern = Pattern.compile(path);
            Matcher matcher = pattern.matcher(requestURI); //요청 uri와 path를 mathcer로 비교

            if (matcher.find()) {
                Long partyId = Long.parseLong(matcher.group(1));

                if (!checkHasRole(partyId, me, request, path)) {
                    log.info("안돼요 돌아가세요");
                    throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, ErrorCode.FORBIDDEN_ERROR.getMessage());
                }
            }
        }
        return true;
    }

    private boolean checkHasRole(Long partyId, Member me, HttpServletRequest request, String pathUrl) {
        String mapKey = pathUrl.replaceAll("[^a-zA-Z0-9]", "");

        Party party = partyRepository.findParty(partyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "파티가 존재하지 않습니다."));

        //권한이랑 상관없는 method => 통과
        if (!pathPropertiesConfig.getPathSecurity().get(mapKey).containsKey(request.getMethod()))
            return true;

        PartyMember partyMember = partyMemberRepository.findByPartyAndMember(party, me).orElse(null);


        //권한 가능 role에 포함되는지
        if (pathPropertiesConfig.getPathSecurity().get(mapKey).get(request.getMethod()).getInclude() != null) {
            if (!pathPropertiesConfig.getPathSecurity().get(mapKey).get(request.getMethod()).getInclude().contains(partyMember.getRole().toString())) {
                return false;
            }
        } else if (pathPropertiesConfig.getPathSecurity().get(mapKey).get(request.getMethod()).getExclude() != null) {
            if (partyMember != null) {

                if (pathPropertiesConfig.getPathSecurity().get(mapKey).get(request.getMethod()).getExclude().contains(partyMember.getRole().toString())) {
                    return false;
                }
            } else {
                if (pathPropertiesConfig.getPathSecurity().get(mapKey).get(request.getMethod()).getExclude().contains("user")) {
                    return false;
                }
            }
        }

        return true;
    }
}
