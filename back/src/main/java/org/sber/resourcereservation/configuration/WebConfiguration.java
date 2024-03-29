package org.sber.resourcereservation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс конфигурации пользовательского веб-слоя.
 */
@Configuration
@Profile({"dev", "default"})
public class WebConfiguration implements WebMvcConfigurer {
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;


    /**
     * Настраивает CORS (Cross-Origin Resource Sharing) для разрешенных источников.
     * Использует параметр 'cors.allowed.origins' для разрешения CORS.
     * @param registry Реестр CorsRegistry для настройки CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedOrigins(allowedOrigins.split(","));
    }
}
