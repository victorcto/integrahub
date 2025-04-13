package br.com.integrahub.services.impls;

import br.com.integrahub.dtos.OAuthResponse;
import br.com.integrahub.entities.OAuthToken;
import br.com.integrahub.repositories.TokenRepository;
import br.com.integrahub.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    @Override
    public void save(OAuthResponse oauth) {
        OAuthToken token = OAuthToken.from(oauth);
        tokenRepository.save(token);
    }

    @Override
    public Optional<OAuthToken> getLatestOAuthToken() {
        return tokenRepository.findTopByOrderByExpiresAtDesc()
                .filter(token -> token.getExpiresAt().isAfter(Instant.now()));
    }
}
