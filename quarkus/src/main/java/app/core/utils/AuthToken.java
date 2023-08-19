package app.core.utils;

import app.quarkus.model.person.User;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthToken {

    @ConfigProperty(name = "mp.jwt.verify.issuer")

    public String issuer;
    Set<String> roles = new HashSet<>();

    public String GenerateAccessToken(User pUsuario) {

        Instant accessExpiresAt = Instant.now().plus(Duration.ofMinutes(10));

        if (pUsuario.hasRole()) {
            pUsuario.role.forEach(c -> roles.add(c.role));
        }
        return Jwt.issuer(this.issuer)
                .upn(pUsuario.login)
                .groups(roles)
                .expiresAt(accessExpiresAt)
                .sign();
    }

    public String GenerateRefreshToken(User pUsuario) {

        Instant refreshExpiresAt = Instant.now().plus(Duration.ofHours(12));

        if (pUsuario.hasRole()) {
            pUsuario.role.forEach(c -> roles.add(c.role));
        }

        return Jwt.issuer(this.issuer)
                .upn(pUsuario.login)
                .groups(roles)
                .expiresAt(refreshExpiresAt)
                .sign();
    }
}
