package com.example.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
     OpenAPI customOpenAPI() {
        return new OpenAPI()
          .info(new Info()
            .title("Auth & Customer Microservices API")
            .version("v1.0.0")
            .description("Documentación centralizada para los microservicios de autenticación y clientes.")
            .termsOfService("https://example.com/terms")
            .contact(new Contact()
                .name("Equipo de Backend")
                .email("backend-team@example.com")
                .url("https://example.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0")))
    
          .addServersItem(new Server()
              .url("https://api.example.com")
              .description("Entorno de producción"))
          .addServersItem(new Server()
              .url("https://staging-api.example.com")
              .description("Entorno de staging"))
       
          .addTagsItem(new Tag()
              .name("Auth Service")
              .description("Operaciones de autenticación y autorización"))
          .addTagsItem(new Tag()
              .name("Customer Service")
              .description("Operaciones de gestión de clientes"));
    }

    @Bean
     GroupedOpenApi authServiceApi() {
        return GroupedOpenApi.builder()
            .group("Auth Service")
            .packagesToScan("com.example.auth_service.controller")
            .pathsToMatch("/auth/**")
            .build();
    }

    @Bean
    GroupedOpenApi customerServiceApi() {
        return GroupedOpenApi.builder()
            .group("Customer Service")
            .packagesToScan("com.example.customer_service.controller")
            .pathsToMatch("/customers/**")
            .build();
    }

}
