package com.homegravity.Odi.global.jwt.handler;

import com.homegravity.Odi.global.jwt.entity.RefreshToken;
import com.homegravity.Odi.global.jwt.repository.RefreshRepository;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Value("${FRONT_BASE_URL}")
    private String baseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //유저 정보
        String username = authentication.getName();

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String memberId = Long.toString(oAuth2User.getId());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access",  role, memberId, 3000000L);
        String refresh = jwtUtil.createJwt("refresh",  role, memberId, 86400000L);

        //Refresh 토큰 저장
        addRefreshEntity(username, refresh, 86400000L);

        //응답 설정
        // response.setHeader(HttpHeaders.AUTHORIZATION, access);
        response.addCookie(createAccessTokenCookie(HttpHeaders.AUTHORIZATION, access));
        response.addCookie(createRefreshTokenCookie("RefreshToken", refresh));

        log.warn("access ==== {}", access);
        log.warn("refresh === {}", refresh);

        response.sendRedirect(baseUrl + "/auth");

        //super.onAuthenticationSuccess(request, response, authentication);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());

        refreshRepository.save(refreshToken);
    }

    private Cookie createAccessTokenCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        //cookie.setHttpOnly(true);

        return cookie;
    }

    private Cookie createRefreshTokenCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
