package com.homegravity.Odi.global.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties("pathallow")
public class PathPropertiesConfig {
    private Map<String, Map<String, Permission>> pathSecurity;


    @Getter
    @AllArgsConstructor
    @ToString
    public static class Permission {
        private List<String> include;
        private List<String> exclude;
    }
}
