package com.homegravity.Odi.global.security.filter;

import com.homegravity.Odi.domain.member.entity.Member;
import com.homegravity.Odi.domain.member.repository.MemberRepository;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 특정 경로 권한 확인 필터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PathFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("일단 필터에 들어왔으요...!!");

        String requestUrl = request.getRequestURI();
        //Long memberId = Long.valueOf(jwtUtil.getId(request.getHeader(HttpHeaders.AUTHORIZATION)));
        //Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
        //        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_ID_NOT_EXIST, ErrorCode.MEMBER_ID_NOT_EXIST.getMessage()));


        log.info("requestUrl: {}", requestUrl);

    }
}
