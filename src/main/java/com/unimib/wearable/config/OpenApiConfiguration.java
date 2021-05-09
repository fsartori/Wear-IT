package com.unimib.wearable.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI wearableOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("wearable API")
                .description("This API allows to communicate with Kaa")
                .version("1"));
    }
}
