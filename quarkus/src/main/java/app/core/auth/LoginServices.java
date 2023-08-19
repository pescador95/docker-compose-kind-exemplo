package app.core.auth;

import app.core.utils.AuthToken;
import app.quarkus.model.person.User;

import javax.inject.Inject;

public class LoginServices {

    @Inject
    AuthToken authToken;
    User user;

    public void login() {

        authToken.GenerateAccessToken(user);
        {

        }
    }
}
