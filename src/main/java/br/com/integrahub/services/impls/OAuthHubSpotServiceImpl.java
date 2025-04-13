package br.com.integrahub.services.impls;

import br.com.integrahub.dtos.HubspotErrorResponse;
import br.com.integrahub.dtos.OAuthResponse;
import br.com.integrahub.exceptions.DataValidationException;
import br.com.integrahub.exceptions.HubSpotException;
import br.com.integrahub.exceptions.IntegraHubException;
import br.com.integrahub.services.OAuthService;
import br.com.integrahub.services.TokenService;
import br.com.integrahub.utils.HubspotProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class OAuthHubSpotServiceImpl implements OAuthService {
    private static final String AUTH_URL = "https://app.hubspot.com/oauth/authorize";
    private static final String TOKEN_URL = "https://api.hubapi.com/oauth/v1/token";
    private static final String GRANT_TYPE = "authorization_code";

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final HubspotProperties properties;
    private final TokenService tokenService;

    @Override
    public String generateAuthorizationUrl() {
        if (properties.getClientId().isBlank() || properties.getScopes().isBlank() || properties.getRedirectUri().isBlank()) {
            throw new DataValidationException("Not all required fields are provided.");
        }

        return UriComponentsBuilder.fromUriString(AUTH_URL)
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("scope", properties.getScopes())
                .toUriString();
    }

    @Transactional
    @Override
    public OAuthResponse callback(String code) {
        validateCallback(code);
        HttpEntity<MultiValueMap<String, String>> request = buildRequestEntity(code);
        try {
            ResponseEntity<OAuthResponse> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, OAuthResponse.class);
            OAuthResponse oauthResponse = response.getBody();
            tokenService.save(oauthResponse);
            return oauthResponse;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw handleHubspotException(e);
        }
    }

    private void validateCallback(String code) {
        if (code == null || code.isBlank()) {
            throw new DataValidationException("Authorization code is required");
        }
    }

    private HttpEntity<MultiValueMap<String, String>> buildRequestEntity(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(body, headers);
    }

    private HubSpotException handleHubspotException(HttpStatusCodeException e) {
        try {
            String responseBody = e.getResponseBodyAsString();
            if (!responseBody.isBlank()) {
                HubspotErrorResponse hubspotError = objectMapper.readValue(responseBody, HubspotErrorResponse.class);
                throw new HubSpotException(hubspotError, e.getStatusCode());
            }

            throw new HubSpotException(e.getMessage(), e.getStatusCode());
        } catch (JsonProcessingException ex) {
            throw new IntegraHubException("Erro ao processar resposta de erro do HubSpot");
        }
    }

}
