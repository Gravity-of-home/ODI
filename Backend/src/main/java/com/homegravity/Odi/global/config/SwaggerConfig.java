package com.homegravity.Odi.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.swagger-ui.info.title}")
    private String title;
    @Value("${springdoc.swagger-ui.info.description}")
    private String description;
    @Value("${springdoc.swagger-ui.info.version}")
    private String version;

    @Bean
    public OpenAPI openAPI() {

//        SecurityRequirement addSecurityItem = new SecurityRequirement();
//        addSecurityItem.addList("Authorization");

        return new OpenAPI()
//                .components(components())
//                .addSecurityItem(addSecurityItem)
                .info(apiInfo());
    }
//
//    private Components components() {
//
//        return new Components()
//                .addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme()
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                        .name(HttpHeaders.AUTHORIZATION)
//                        .in(SecurityScheme.In.HEADER))
//                .addSecuritySchemes("RefreshToken", new SecurityScheme()
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("cookie")
//                        .name("RefreshToken")
//                        .in(SecurityScheme.In.COOKIE));
//    }

    private Info apiInfo() {

        return new Info()
                .title(title)
                .description(description)
                .version(version);
    }

}
