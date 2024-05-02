package com.homegravity.Odi.global.jwt.controller;

import com.homegravity.Odi.global.jwt.entity.RefreshToken;
import com.homegravity.Odi.global.jwt.repository.RefreshRepository;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.response.error.ErrorCode;
import com.homegravity.Odi.global.response.error.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //todo: service단으로 나누기
        //get refresh token

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RefreshToken")) {
                refresh = cookie.getValue();
            }
        }


        if (refresh == null) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String id = jwtUtil.getId(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", role, id, 86400000L);
        String newRefresh = jwtUtil.createJwt("refresh", role, id, 345600000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        //todo access token들을 기억한 뒤 블랙리스트 처리를 진행하는 로직 작성해야 함
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefresh, 345600000L);

        //response
        //response.setHeader(HttpHeaders.AUTHORIZATION, newAccess);
        response.addCookie(createAccessTokenCookie(HttpHeaders.AUTHORIZATION, newAccess));
        response.addCookie(createRefreshTokenCookie("RefreshToken", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
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
        cookie.setMaxAge(4 * 24 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        //cookie.setHttpOnly(true);

        return cookie;
    }

    private Cookie createRefreshTokenCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(4 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
