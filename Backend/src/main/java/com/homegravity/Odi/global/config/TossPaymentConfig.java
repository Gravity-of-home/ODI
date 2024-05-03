package com.homegravity.Odi.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TossPaymentConfig {

    @Value("${payment.toss.test_client_api_key}")
    private String testClientKey;

    @Value("${payment.toss.test_secret_api_key}")
    private String testSecretKey;

    public static final String TOSS_PAYMENT_URL = "https://api.tosspayments.com/v1/payments/confirm";
}
