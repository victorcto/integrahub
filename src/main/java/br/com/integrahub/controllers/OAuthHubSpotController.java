package br.com.integrahub.controllers;

import br.com.integrahub.dtos.OAuthResponse;
import br.com.integrahub.services.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth/hubspot")
@RestController
public class OAuthHubSpotController {
    private final OAuthService oauthservice;

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizationUrl() {
        return ResponseEntity.ok(oauthservice.generateAuthorizationUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<OAuthResponse> callback(@RequestParam String code) {
        return ResponseEntity.ok(oauthservice.callback(code));
    }
}
