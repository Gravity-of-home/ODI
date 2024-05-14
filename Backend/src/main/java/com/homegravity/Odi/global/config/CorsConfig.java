package com.homegravity.Odi.global.config;

import com.homegravity.Odi.global.security.handler.PathHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${FRONT_BASE_URL}")
    private String frontBaseUrl;

    @Autowired
    private PathHandlerInterceptor pathHandlerInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("*") // 허용할 출처
                .allowedHeaders("*")
                .allowedMethods("*"); // 허용할 HTTP 메소드
//                .allowedOrigins(frontBaseUrl);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pathHandlerInterceptor).addPathPatterns("/api/parties/**");
    }
}
