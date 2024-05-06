package com.homegravity.Odi.global.config;

import com.homegravity.Odi.global.jwt.filter.CustomLogoutFilter;
import com.homegravity.Odi.global.jwt.filter.JWTFilter;
import com.homegravity.Odi.global.jwt.handler.CustomSuccessHandler;
import com.homegravity.Odi.global.jwt.repository.RefreshRepository;
import com.homegravity.Odi.global.jwt.util.JWTUtil;
import com.homegravity.Odi.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;
    private final CustomLogoutFilter logoutFilter;

    @Value("${FRONT_BASE_URL}")
    private String baseUrl;

    @Value("${WHITE_LIST}")
    private String[] whiteList;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // click jacking 방지
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // csrf 설정 비활성화 -> jwt 방식을 사용하기 때문
                .csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable())
                // cors 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Form 로그인 방식 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP Basic 인증 방식 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(whiteList).permitAll()
                        .anyRequest().authenticated())
                // OAuth
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig   // OAuth 2.0 인증 후 사용자 정보를 가져오는 엔드포인트
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))  // OAuth 2.0 로그인 성공 후에 수행될 커스텀 핸들러
                // 로그인 후에 JWTFilter로 검증
                .addFilterAfter(jwtFilter, OAuth2LoginAuthenticationFilter.class)
                // 로그아웃
                .addFilterBefore(logoutFilter, LogoutFilter.class)
                // RESTful API
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    CorsConfigurationSource corsConfigurationSource() {

        return request -> {
            CorsConfiguration config = new CorsConfiguration();

            // FRONT 주소 허용
            config.setAllowedOrigins(Collections.singletonList(baseUrl));
//            config.setAllowedOrigins(Collections.singletonList(baseUrlAI));
            // 모든 REST Method 허용
            config.setAllowedMethods(Collections.singletonList("*"));
            // credential 값 허용
            config.setAllowCredentials(true);
            // 모든 header 허용
            config.setAllowedHeaders(Collections.singletonList("*"));
            // preflight 요청의 결과를 캐시할 시간 지정
            config.setMaxAge(3600L);

            // 브라우저에 response header에 포함할 목록
            config.setExposedHeaders(Collections.singletonList("Set-Cookie"));
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            return config;
        };
    }
}