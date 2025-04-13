package br.com.integrahub.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthResponse(@JsonProperty("access_token") String accessToken,
                            @JsonProperty("refresh_token") String refreshToken,
                            @JsonProperty("expires_in") Long expiresIn) {
}
