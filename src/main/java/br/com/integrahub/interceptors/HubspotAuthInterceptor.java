package br.com.integrahub.interceptors;

import br.com.integrahub.entities.OAuthToken;
import br.com.integrahub.exceptions.AuthenticationException;
import br.com.integrahub.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class HubspotAuthInterceptor implements ClientHttpRequestInterceptor {
    private static final String HUBSPOT_API_BASE_URL = "https://api.hubapi.com/crm";
    private final TokenService tokenService;

    @NonNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        @NonNull byte[] body,
                                        @NonNull ClientHttpRequestExecution execution) throws IOException {
        if (request.getURI().toString().startsWith(HUBSPOT_API_BASE_URL)) {
            Optional<OAuthToken> tokenOpt = tokenService.getLatestOAuthToken();
            if (tokenOpt.isPresent()) {
                request.getHeaders().setBearerAuth(tokenOpt.get().getAccessToken());
                request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            } else {
                throw new AuthenticationException("Access token not available");
            }
        }

        return execution.execute(request, body);
    }
}
