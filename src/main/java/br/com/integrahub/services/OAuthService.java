package br.com.integrahub.services;

import br.com.integrahub.dtos.OAuthResponse;

public interface OAuthService {
    String generateAuthorizationUrl();

    OAuthResponse callback(String code);
}
