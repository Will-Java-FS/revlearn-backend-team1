package com.revlearn.team1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration());
        return source;
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        // TODO handle csrf properly

        // TODO add any necessary EC2 URLs to this list
        List<String> allowedOrigins = List.of(
                "http://localhost:5173",
                "http://localhost:8080",
                "http://www.revaturelearn.com",
                "https://www.revaturelearn.com",
                "http://api.revaturelearn.com", //This is just to query a development build from the production swagger ui
                "https://api.revaturelearn.com",//This is just to query a development build from the production swagger ui
                "http://revaturelearn.com",
                "https://revaturelearn.com");

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
}
