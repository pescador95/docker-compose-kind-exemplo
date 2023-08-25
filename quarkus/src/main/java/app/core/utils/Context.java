package app.core.utils;

import app.quarkus.model.person.User;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.time.LocalTime;

@ApplicationScoped
@Transactional
public class Context {

    public static LocalDate currentDate() {
        return LocalDate.now();
    }

    public static LocalTime currentTime() {
        return LocalTime.now();
    }

    public static User getContextUser(@javax.ws.rs.core.Context @NotNull SecurityContext context) {

        if (BasicFunctions.isNotEmpty(context.getUserPrincipal())) {

            String login = context.getUserPrincipal().getName();
            return User.find("login = ?1 and active = true", login.toLowerCase()).firstResult();
        }
        return User.find("bot = true and active = true").firstResult();
    }

    public static String getContextUserKey(@javax.ws.rs.core.Context @NotNull
                                           SecurityContext context) {
        if (BasicFunctions.isNotEmpty(context.getUserPrincipal())) {
            return ((DefaultJWTCallerPrincipal) context.getUserPrincipal()).getRawToken();
        }
        return "";
    }

    public static Boolean isUserAdmin(User pUser) {
        User user = User.find("login = ?1 and active = true", pUser.login.toLowerCase()).firstResult();

        if (user.hasRole()) {
            return user.admin();
        }
        return false;
    }

    public static Boolean isUserBot(User pUser) {
        User user = User.find("login = ?1 and active = true", pUser.login.toLowerCase()).firstResult();

        if (user.hasRole()) {
            return !user.user();
        }
        return false;
    }
}
