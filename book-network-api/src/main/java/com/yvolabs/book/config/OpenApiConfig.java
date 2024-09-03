 package com.yvolabs.book.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 02/09/2024
 */

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Yvolabs",
                        email = "contact@yvolabs.com",
                        url = "https://yvolabs.com"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi Specification - Book-Network-API @yvolabs",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://produrl/api/v1"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

@Configuration
public class OpenApiConfig {


    @Bean
    public OperationCustomizer operationCustomizer() {
        // Set the operation summary based on the method name
        return (operation, handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();
            operation.setSummary(generateSummaryFromMethodName(methodName));
            return operation;
        };
    }


    private String generateSummaryFromMethodName(String methodName) {
        // Convert camelCase or PascalCase method name to a human-readable summary
        return methodName.replaceAll("([a-z])([A-Z]+)", "$1 $2");
    }
}
