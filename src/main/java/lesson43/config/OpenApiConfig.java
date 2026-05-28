package lesson43.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Product REST API",
                description = "REST API for managing products and uploading files",
                version = "1.0.0",
                contact = @Contact(
                        name = "Student Poly"
                )
        )
)
public class OpenApiConfig { }