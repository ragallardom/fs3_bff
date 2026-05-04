package cl.innovatech.bffservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient rrhhWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081/api/rrhh").build();
    }

    @Bean
    public WebClient proyectosWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8082/api/proyectos").build();
    }
}