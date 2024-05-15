package com.homegravity.Odi.global.config;

import com.homegravity.Odi.global.security.handler.PathHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthorityConfig implements WebMvcConfigurer {

    private final PathHandlerInterceptor pathHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pathHandlerInterceptor).addPathPatterns("/api/parties/**");
    }
}
