package br.com.integrahub.services;

import br.com.integrahub.dtos.OAuthResponse;
import br.com.integrahub.entities.OAuthToken;

import java.util.Optional;

public interface TokenService {
    void save(OAuthResponse oauth);

    Optional<OAuthToken> getLatestOAuthToken();
}
