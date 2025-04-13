package br.com.integrahub.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "hubspot")
public class HubspotProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;
}
