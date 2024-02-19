package org.kharisov.configs;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Конфигурация Swagger для вашего приложения.
 * Swagger используется для автоматической генерации документации API.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * Создает и настраивает новый экземпляр Docket, который Swagger использует для генерации документации API.
     *
     * @return новый экземпляр Docket.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
