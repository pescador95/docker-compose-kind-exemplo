package app.core.controller.auth;

import app.core.model.DTO.Responses;
import app.core.model.auth.Auth;
import app.core.model.auth.Role;
import app.core.utils.AuthToken;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.person.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AuthController {

    @Inject
    AuthToken token;

    @Inject
    JWTParser parser;

    List<Role> roles;

    private final List<String> rolesString = new ArrayList<>();

    private Responses responses = new Responses();

    public Response login(Auth data) {

        roles = new ArrayList<>();
        roles = new ArrayList<>();
        User user;
        responses.messages = new ArrayList<>();

        if (BasicFunctions.isNotEmpty(data) && BasicFunctions.isNotEmpty(data.login)) {

            user = User.find("login = ?1", data.login.toLowerCase()).firstResult();

        } else {
            responses.status = 400;
            responses.data = data;
            responses.messages.add("incorrect credentials.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
        if (user.hasRole()) {
            user.role.forEach(c -> rolesString.add(c.role));
        }

        if (BasicFunctions.isEmpty(user)) {
            responses.status = 400;
            responses.data = data;
            responses.messages.add("incorrect credentials.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        boolean authenticated = BCrypt.checkpw(data.password, user.password);

        if (!authenticated) {
            responses.status = 400;
            responses.data = data;
            responses.messages.add("incorrect credentials.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        System.out.println("\n" + "logging with user: " + user.login + "..." + "\n"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:kk:ss")) + "\n" + "Welcome, "
                + user.login + "!");
        String accessToken = token.GenerateAccessToken(user);
        String refreshToken = token.GenerateRefreshToken(user);

        Long ACTOKEN;
        Long RFTOKEN;

        try {
            ACTOKEN = parser.parse(accessToken).getClaim("exp");
            RFTOKEN = parser.parse(refreshToken).getClaim("exp");
        } catch (ParseException e) {
            responses.status = 400;
            responses.data = data;
            responses.messages.add("incorrect credentials.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        Auth auth = new Auth();
        responses = new Responses();
        auth.login = user.login;
        auth.password = BcryptUtil.bcryptHash(user.password);
        auth.role = new ArrayList<>();
        auth.roles = new ArrayList<>();
        auth.role.addAll(user.role);
        auth.roles.addAll(rolesString);
        auth.admin = Context.isUserAdmin(user);
        auth.accessToken = accessToken;
        auth.refreshToken = refreshToken;
        auth.user = user;
        System.out.println("\n" + "Re-authenticating user login: " + user.login + "..." + "\n"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:kk:ss")) + "\n"
                + "Welcome back, " + user.login + "!");
        auth.expireDateAccessToken = LocalDateTime.ofEpochSecond(ACTOKEN, 0, ZoneOffset.UTC);
        auth.expireDateRefreshToken = LocalDateTime.ofEpochSecond(RFTOKEN, 0, ZoneOffset.UTC);
        responses.status = 200;
        responses.data = auth;
        return Response.ok(responses).status(Response.Status.ACCEPTED).build();
    }

    public Response refreshToken(Auth data) {
        User user;
        LocalDateTime expireDate;
        roles = new ArrayList<>();
        roles = new ArrayList<>();
        responses.messages = new ArrayList<>();

        try {
            String login = parser.parse(data.refreshToken).getClaim("upn");
            long expireDateOldToken = parser.parse(data.refreshToken).getClaim("exp");

            user = User.find("login", login).firstResult();

            if (user.hasRole()) {
                user.role.forEach(c -> rolesString.add(c.role));
            }

            expireDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(expireDateOldToken), ZoneOffset.UTC);

            if (expireDate.isAfter(LocalDateTime.now()) && BasicFunctions.isNotEmpty(user)) {
                String accessToken = token.GenerateAccessToken(user);
                String refreshToken = token.GenerateRefreshToken(user);
                System.out.print("Update token requested by user: " + user.login + "\n");
                Long ACTOKEN;
                Long RFTOKEN;
                try {
                    ACTOKEN = parser.parse(accessToken).getClaim("exp");
                    RFTOKEN = parser.parse(refreshToken).getClaim("exp");
                } catch (ParseException e) {
                    responses.status = 400;
                    responses.data = data;
                    responses.messages.add("incorrect credentials.");
                    return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
                }
                Auth auth = new Auth();
                auth.login = user.login;
                auth.role = new ArrayList<>();
                auth.roles = new ArrayList<>();
                auth.role.addAll(user.role);
                auth.roles.addAll(rolesString);
                auth.admin = Context.isUserAdmin(user);
                auth.user = user;
                auth.accessToken = accessToken;
                auth.expireDateAccessToken = LocalDateTime.ofEpochSecond(ACTOKEN, 0, ZoneOffset.UTC);
                auth.refreshToken = refreshToken;
                auth.expireDateRefreshToken = LocalDateTime.ofEpochSecond(RFTOKEN, 0, ZoneOffset.UTC);
                responses.status = 200;
                responses.data = auth;
                return Response.ok(responses).status(Response.Status.ACCEPTED).build();
            } else {
                responses.status = 400;
                responses.data = data;
                responses.messages.add("incorrect credentials.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
        } catch (ParseException error) {
            System.out.print(error.getMessage());
        }
        return Response.ok(responses).status(Response.Status.ACCEPTED).build();
    }
}
