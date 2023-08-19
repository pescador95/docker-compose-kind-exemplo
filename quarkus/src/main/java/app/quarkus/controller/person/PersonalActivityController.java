package app.quarkus.controller.person;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.person.Person;
import app.quarkus.model.person.PersonalActivity;
import app.quarkus.model.person.User;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class PersonalActivityController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private PersonalActivity personalActivity = new PersonalActivity();
    private Responses responses;
    private User userAuth;
    private Person person;

    public Response addPersonalActivity(@NotNull PersonalActivity pPersonalActivity) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);
        personalActivity = PersonalActivity.find("person = ?1 and active = true", pPersonalActivity.person)
                .firstResult();
        if (BasicFunctions.isNotEmpty(pPersonalActivity.person)
                && pPersonalActivity.person.isValid()) {
            person = Person.findById(pPersonalActivity.person.id);
        }

        if (BasicFunctions.isEmpty(personalActivity) && BasicFunctions.isNotEmpty(person)) {
            personalActivity = new PersonalActivity();

            if (BasicFunctions.isNotEmpty(person)) {
                personalActivity.person = person;
            }
            if (BasicFunctions.isNotEmpty(pPersonalActivity.occupation)) {
                personalActivity.occupation = pPersonalActivity.occupation;
            }
            if (BasicFunctions.isNotEmpty(pPersonalActivity.responsibleContact)) {
                personalActivity.responsibleContact = pPersonalActivity.responsibleContact;
            }
            if (!responses.hasMessages()) {
                personalActivity.personName = person.name;
                personalActivity.user = userAuth;
                personalActivity.updatedBy = userAuth;
                personalActivity.active = Boolean.TRUE;
                personalActivity.updatedAt = LocalDateTime.now();
                personalActivity.persist();

                responses.status = 201;
                responses.data = personalActivity;
                responses.messages.add("PersonalActivity registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = personalActivity;
            responses.messages.add("PersonalActivity already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updatePersonalActivity(@NotNull PersonalActivity pPersonalActivity) {

        User userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        personalActivity = PersonalActivity.find("id = ?1 and active = true", pPersonalActivity.id).firstResult();

        if (BasicFunctions.isNotEmpty(pPersonalActivity.person)
                && pPersonalActivity.person.isValid()) {
            person = Person.findById(pPersonalActivity.person.id);
        }

        try {

            if (!pPersonalActivity.isValid() && BasicFunctions.isEmpty(pPersonalActivity.person)
                    && BasicFunctions.isEmpty(pPersonalActivity.person)
                    && BasicFunctions.isEmpty(pPersonalActivity.personName)
                    && BasicFunctions.isEmpty(pPersonalActivity.responsibleContact)
                    && BasicFunctions.isEmpty(pPersonalActivity.occupation)) {
                throw new BadRequestException("Enter data for update the registration of PersonalActivity.");
            } else {
                if (BasicFunctions.isNotEmpty(pPersonalActivity.person)) {
                    personalActivity.person = pPersonalActivity.person;
                }
                if (BasicFunctions.isNotEmpty(pPersonalActivity.occupation)) {
                    personalActivity.occupation = pPersonalActivity.occupation;
                }
                if (BasicFunctions.isNotEmpty(pPersonalActivity.responsibleContact)) {
                    personalActivity.responsibleContact = pPersonalActivity.responsibleContact;
                }
                personalActivity.personName = person.name;
                personalActivity.updatedBy = userAuth;
                personalActivity.updatedAt = LocalDateTime.now();
                personalActivity.persist();

                responses.status = 200;
                responses.data = personalActivity;
                responses.messages.add("Cadastro de PersonalActivity updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = personalActivity;
            responses.messages.add("cannot update o cadastro PersonalActivity.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deletePersonalActivity(@NotNull List<Long> pListIdPersonalActivity) {

        List<PersonalActivity> personalActivities;
        List<PersonalActivity> clientesAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        personalActivities = PersonalActivity.list("id in ?1 and active = true", pListIdPersonalActivity);
        int count = personalActivities.size();

        try {

            if (personalActivities.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Históricos not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            personalActivities.forEach((personalActivity) -> {

                personalActivity.updatedBy = userAuth;
                personalActivity.active = Boolean.FALSE;
                personalActivity.updatedAt = LocalDateTime.now();
                personalActivity.deletedAt = LocalDateTime.now();
                personalActivity.persist();
                clientesAux.add(personalActivity);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = personalActivity;
                responses.messages.add("Histórico successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(clientesAux);
                responses.messages.add(count + " Históricos successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = personalActivity;
                responses.messages.add("Histórico not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(personalActivities);
                responses.messages.add("Históricos not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivatePersonalActivity(@NotNull List<Long> pListIdPersonalActivity) {

        List<PersonalActivity> personalActivities;
        List<PersonalActivity> clientesAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        User userAuth = Context.getContextUser(context);
        personalActivities = PersonalActivity.list("id in ?1 and active = false", pListIdPersonalActivity);
        int count = personalActivities.size();

        if (personalActivities.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Personal Activity not located or already reactivated.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {
            personalActivities.forEach((cliente) -> {

                cliente.updatedBy = userAuth;
                cliente.active = Boolean.TRUE;
                cliente.updatedAt = LocalDateTime.now();
                cliente.deletedAt = LocalDateTime.now();
                cliente.persist();
                clientesAux.add(cliente);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = personalActivity;
                responses.messages.add("PersonalActivity reactived with sucessful!");
            } else {
                responses.datas = Collections.singletonList(clientesAux);
                responses.messages.add(count + " PersonalActivity reactived with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = personalActivity;
                responses.messages.add("Personal Activity not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(personalActivities);
                responses.messages.add("Personal Activity not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
