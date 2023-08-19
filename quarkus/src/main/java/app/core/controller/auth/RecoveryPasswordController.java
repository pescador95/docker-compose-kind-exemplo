package app.core.controller.auth;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.person.Person;
import app.quarkus.model.person.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import net.bytebuddy.utility.RandomString;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ApplicationScoped
@Transactional
public class RecoveryPasswordController {
    @Inject
    Mailer mailer;
    @javax.ws.rs.core.Context
    SecurityContext context;
    User userAuth;
    private Responses responses;

    public Response sendEmail(String login) {
        responses = new Responses();
        responses.messages = new ArrayList<>();

        User user = User.find("login = ?1 and active = true", login.toLowerCase()).firstResult();
        Person person = Person.findById(user.person.id);
        if (user.active && BasicFunctions.isNotEmpty(person)) {
            String senha = RandomString.make(12);
            System.out.println(senha);
            user.password = BcryptUtil.bcryptHash(senha);
            user.changePassword = Boolean.TRUE;
            user.persist();
            String name = person.name;
            mailer.send(Mail.withText(person.email, "Quarkus App - Recovery Password", "Hi, " + name + "!\n"
                    + "Here is your new password: " + senha));
            responses.status = 200;
            responses.data = user;
            responses.messages.add("send a mail to: " + person.email);
        } else {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("Unable to locate a record with the email provided.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(responses).status(Response.Status.ACCEPTED).build();
    }

    public Response updatePassword(String password) {
        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {
            userAuth = Context.getContextUser(context);
            userAuth.password = BcryptUtil.bcryptHash(password);
            userAuth.updatedBy = userAuth.login;
            userAuth.updatedAt = LocalDateTime.now();
            userAuth.persist();

            responses.status = 200;
            responses.data = userAuth;
            responses.messages.add("Password successfully updated.");
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("Unable to update password.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}