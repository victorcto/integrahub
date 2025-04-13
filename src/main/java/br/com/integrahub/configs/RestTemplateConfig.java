package br.com.integrahub.configs;

import br.com.integrahub.interceptors.HubspotAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {
    private final HubspotAuthInterceptor authInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(authInterceptor));
        return restTemplate;
    }
}
