package com.coplaca.apirest.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                                                .title("Coplaca API")
                                                .version("1.0.0")
                                                .description("""
                                                                API REST modular para la gestión de productos, pedidos, usuarios, logística y recomendaciones de Coplaca.

                                                                Navegación rápida:
                                                                - Auth y sesión
                                                                - Catálogo y ofertas
                                                                - Usuarios y almacenes
                                                                - Pedidos y ETA
                                                                - Administración
                                                                """)
                        .contact(new Contact()
                                                                .name("Coplaca Team")
                                                                .email("support@coplaca.com"))
                                                .license(new License()
                                                                .name("Internal use")
                                                                .url("https://coplaca.local")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Guía técnica y de operación")
                                                .url("/docs/GUIA_OPERATIVA_BACKEND.md"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                                                                .description("JWT token para autenticación. Obtén el token desde el endpoint /auth/login")));
    }

        @Bean
        public GroupedOpenApi authGroup() {
                return groupedApi("01 - Autenticación y sesión", "/auth/**");
        }

        @Bean
        public GroupedOpenApi landingGroup() {
                return groupedApi("02 - Landing y recomendaciones", "/landing/**");
        }

        @Bean
        public GroupedOpenApi catalogGroup() {
                return groupedApi("03 - Catálogo y ofertas", "/api/v1/products/**", "/api/v1/categories/**", "/api/v1/offers/**");
        }

        @Bean
        public GroupedOpenApi operationsGroup() {
                return groupedApi("04 - Pedidos y ETA", "/api/v1/orders/**", "/api/v1/eta/**");
        }

        @Bean
        public GroupedOpenApi peopleGroup() {
                return groupedApi("05 - Usuarios y almacenes", "/api/v1/users/**", "/api/v1/warehouses/**");
        }

        @Bean
        public GroupedOpenApi adminGroup() {
                return groupedApi("06 - Administración", "/api/v1/admin/**");
        }

        private GroupedOpenApi groupedApi(String groupName, String... paths) {
                return GroupedOpenApi.builder()
                                .group(groupName)
                                .pathsToMatch(paths)
                                .build();
        }
}
