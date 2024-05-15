package com.homegravity.Odi.global.jwt.filter;

import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Value("${WHITE_LIST}")
    private String[] whiteList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String path = request.getRequestURI();
//
//        AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//        for (String list : whiteList) {
//            if (antPathMatcher.match(list, path)) {
//                //log.info("pass token filter .....");
//                filterChain.doFilter(request, response);
//                return;
//            }
//        }
        // 헤더에서 토큰 검증

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰이 없다면 다음 필터로 넘김
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = parseBearerToken(authorization);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(jwtUtil.getId(accessToken)), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    public String parseBearerToken(String authorization) {
        return Optional.ofNullable(authorization)
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .orElseThrow(() -> new BusinessException(ErrorCode.JWT_BADTYPE, ErrorCode.JWT_BADTYPE.getMessage()));
    }
}
