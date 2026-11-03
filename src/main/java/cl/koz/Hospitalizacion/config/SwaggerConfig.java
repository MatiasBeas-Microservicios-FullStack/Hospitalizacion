package cl.koz.Hospitalizacion.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8500"))
                .info(new Info()
                        .title("Swagger: Hospitalizacion")
                        .version("1.0")
                        .description("Documentacion de la API para el sistema de 'Hospitalizacion'")
                        .contact(new Contact()
                                .name("Equipo tecnico")
                                .email("comunicaciones.hgf@redsalud.gob.cl")
                                .url("https://www.hospitalfricke.cl/"))
                        .license(new License()
                                .name("Hospital Viña del Mar")
                                .url("https://www.hospitalfricke.cl/Hospitalizacion")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
