package com.arms.config;

import com.arms.config.interceptor.LoginErrorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebFilter myRedirectFilter() {
        return new LoginErrorFilter();
    }

}