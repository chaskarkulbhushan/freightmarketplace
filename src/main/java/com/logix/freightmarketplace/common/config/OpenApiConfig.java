package com.logix.freightmarketplace.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI freightMarketplaceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Freight Marketplace API")
                        .description("REST API for the Freight Capacity Marketplace")
                        .version("1.0.0"));
    }
}
