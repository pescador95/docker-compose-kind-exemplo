package app.core.utils;

import app.core.controller.contract.ContractController;
import app.core.model.contract.TypeContract;
import app.core.services.auth.RedisService;
import app.quarkus.model.person.User;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthToken {

    @ConfigProperty(name = "mp.jwt.verify.issuer")

    public String issuer;
    Set<String> roles = new HashSet<>();

    @Inject
    RedisService redisClient;

    public String GenerateAccessToken(User pUser) {

        Instant accessExpiresAt = Instant.now().plus(Duration.ofMinutes(10));
        int accessSeconds = (int) Duration.between(Instant.now(), accessExpiresAt).getSeconds();

        if (pUser.hasRole()) {
            pUser.role.forEach(c -> roles.add(c.role));
        }
        String accessToken = Jwt.issuer(this.issuer)
                .upn(pUser.login)
                .groups(roles)
                .expiresAt(accessExpiresAt)
                .sign();

        String expiration = Integer.toString(accessSeconds);

        TypeContract typeContract = ContractController.getTypeContractByUserOrganizationDefault(pUser);

        if (BasicFunctions.isNotEmpty(typeContract)) {
            redisClient.setex(pUser, expiration, accessToken, typeContract);
            if (typeContract.sharedSession()) {
                System.out.println("Setting Shared Session...");
            } else {

                System.out.println("Setting Unique Session...");
            }

        }
        return accessToken;
    }

    public String GenerateRefreshToken(User pUser) {

        Instant refreshExpiresAt = Instant.now().plus(Duration.ofHours(12));

        if (pUser.hasRole()) {
            pUser.role.forEach(c -> roles.add(c.role));
        }

        return Jwt.issuer(this.issuer)
                .upn(pUser.login)
                .groups(roles)
                .expiresAt(refreshExpiresAt)
                .sign();
    }
}
