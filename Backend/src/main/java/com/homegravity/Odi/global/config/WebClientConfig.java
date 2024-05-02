package com.homegravity.Odi.global.config;

import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${NAVER_MAP_API_KEY_ID}")
    private String naverKeyId;

    @Value("${NAVER_MAP_API_KEY}")
    private String naverKey;

    @Bean
    public WebClient naverWebClient() {

        // 연결 시간 초과
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        return WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", naverKeyId)
                .defaultHeader("X-NCP-APIGW-API-KEY", naverKey)
                .build();
    }

}
