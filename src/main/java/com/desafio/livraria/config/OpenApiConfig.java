package com.desafio.livraria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springdoc.core.models.GroupedOpenApi;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearer-jwt";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Livraria API")
                        .description("API da Livraria - gerenciamento de autores, gêneros e livros")
                        .version("v1")
                        .contact(new Contact().name("Equipe Livraria").email("dev@livraria.local"))
                        .license(new License().name("MIT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                );
    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi autoresGroup() {
        return GroupedOpenApi.builder()
                .group("autores")
                .pathsToMatch("/autores/**")
                .build();
    }

    @Bean
    public GroupedOpenApi livrosGroup() {
        return GroupedOpenApi.builder()
                .group("livros")
                .pathsToMatch("/api/livros/**")
                .build();
    }

    @Bean
    public GroupedOpenApi generosGroup() {
        return GroupedOpenApi.builder()
                .group("generos")
                .pathsToMatch("/api/generos/**")
                .build();
    }
}


