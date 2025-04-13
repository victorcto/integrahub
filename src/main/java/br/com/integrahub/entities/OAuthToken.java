package br.com.integrahub.entities;

import br.com.integrahub.dtos.OAuthResponse;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "oauth_token")
public class OAuthToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false, unique = true, updatable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true, updatable = false)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Instant expiresAt;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public static OAuthToken from(OAuthResponse oauth) {
        return OAuthToken.builder()
                .accessToken(oauth.accessToken())
                .refreshToken(oauth.refreshToken())
                .expiresAt(Instant.now().plusSeconds(oauth.expiresIn()))
                .build();
    }
}
