package cl.koz.Hospitalizacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClienteRunConfig {
    @Bean
    public WebClient webClientRun() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }
}
