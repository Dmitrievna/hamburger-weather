package com.project.hamburger_weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class CorsConfig {

    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().getHeaders()
                    .add("Access-Control-Allow-Origin", "*");
            return chain.filter(exchange);
        };
    }
}
