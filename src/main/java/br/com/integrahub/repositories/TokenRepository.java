package br.com.integrahub.repositories;

import br.com.integrahub.entities.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<OAuthToken, UUID> {
    Optional<OAuthToken> findTopByOrderByExpiresAtDesc();
}
