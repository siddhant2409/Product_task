package com.productmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(
        info = @Info(
                title = "Product Manager API",
                version = "1.0.0",
                description = "RESTful API for Product Management with JWT Security",
                contact = @Contact(
                        name = "Product Manager Team",
                        email = "support@productmanager.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development"),
                @Server(url = "https://api.productmanager.com", description = "Production")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT token for authentication",
        in = SecuritySchemeIn.HEADER
)
public class ProductManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagerApiApplication.class, args);
    }

}
