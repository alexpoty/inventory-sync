package com.alexpoty.inventory_sync.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Inventory Sync API")
                        .description("REST API for management inventory between warehouses"))
                .tags(List.of(
                        new Tag().name("Warehouse API").description("Operations related to warehouse management"),
                        new Tag().name("Stock Transfer API").description("APIs for managing stock transfers between warehouses"),
                        new Tag().name("Product API").description("Operations related to products management")
                ));
    }
}
