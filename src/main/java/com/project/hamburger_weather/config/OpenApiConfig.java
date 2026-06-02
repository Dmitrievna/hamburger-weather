package com.project.hamburger_weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hamburger Weather")
                        .description("""
                    A cycling route planner that combines:
                    - Route calculation between two addresses
                    - Weather forecast for the next 3 hours
                    - Accident risk assessment based on historical data
                    - Correlation of accidents with weather and light conditions
                    """)
                        .version("1.0.0")
                );
    }
}
