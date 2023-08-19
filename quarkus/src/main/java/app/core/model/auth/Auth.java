package app.core.model.auth;

import app.quarkus.model.person.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class Auth {
    public String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Role> role;
    public List<String> roles;

    public Boolean admin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User user;
    public String accessToken;
    public LocalDateTime expireDateAccessToken;
    public String refreshToken;
    public LocalDateTime expireDateRefreshToken;

}
