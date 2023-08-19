package app.quarkus.controller.person;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.address.Address;
import app.quarkus.model.person.Gender;
import app.quarkus.model.person.Person;
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
public class PersonController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private Person person = new Person();
    private Responses responses;
    private User userAuth;
    private Address address;
    private Gender gender;

    public Response addPerson(@NotNull Person pPerson) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        if (BasicFunctions.isNotEmpty(pPerson.address)) {
            address = Address.findById(pPerson.address.id);
        }
        if (BasicFunctions.isNotEmpty(pPerson.gender)) {
            gender = Gender.findById(pPerson.gender.id);
        }
        if (BasicFunctions.isNotEmpty(pPerson.document)) {
            person = Person.find("cpf = ?1 and active = true", pPerson.document).firstResult();
        }
        if (BasicFunctions.isEmpty(person)) {
            person = new Person();

            if (BasicFunctions.isNotEmpty(pPerson.name)) {
                person.name = pPerson.name;
            }
            if (BasicFunctions.isNotEmpty(gender)) {
                person.gender = gender;
            }
            if (BasicFunctions.isNotEmpty(pPerson.birthday)) {
                person.birthday = pPerson.birthday;
            }
            if (BasicFunctions.isNotEmpty(pPerson.telephone)) {
                person.telephone = pPerson.telephone;
            }
            if (BasicFunctions.isNotEmpty(pPerson.cellphone)) {
                person.cellphone = pPerson.cellphone;
            }
            if (BasicFunctions.isNotEmpty(pPerson.email)) {
                person.email = pPerson.email;
            }
            if (BasicFunctions.isNotEmpty(address)) {
                person.address = address;
            }

            if (!responses.hasMessages()) {
                responses.messages = new ArrayList<>();
                person.document = pPerson.document;
                person.user = userAuth;
                person.updatedBy = userAuth;
                person.active = Boolean.TRUE;
                person.updatedAt = LocalDateTime.now();
                person.persist();

                responses.status = 201;
                responses.data = person;
                responses.messages.add("Person registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = person;
            responses.messages.add("Person already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updatePerson(@NotNull Person pPerson) throws BadRequestException {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (BasicFunctions.isNotEmpty(pPerson) && BasicFunctions.isNotEmpty(pPerson.address)) {
                address = Address.findById(pPerson.address.id);
            }
            if (BasicFunctions.isNotEmpty(pPerson) && BasicFunctions.isNotEmpty(pPerson.gender)) {
                gender = Gender.findById(pPerson.gender.id);
            }
            if (BasicFunctions.isNotEmpty(pPerson) && pPerson.isValid()) {
                person = Person.findById(pPerson.id);
            }

            if (BasicFunctions.isEmpty(person)) {
                responses.messages.add("The person which you want to change the data was not found!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            } else {
                if (BasicFunctions.isNotEmpty(pPerson.name)) {
                    if (!person.name.equals(pPerson.name)) {
                        person.name = pPerson.name;
                    }
                }
                if (BasicFunctions.isNotEmpty(pPerson.gender)) {
                    if (!person.gender.equals(gender)) {
                        person.gender = gender;
                    }
                }

                if (BasicFunctions.isNotEmpty(pPerson.document)) {
                    if (!person.document.equals(pPerson.document)) {
                        person.document = pPerson.document;
                    }
                }

                if (BasicFunctions.isNotEmpty(pPerson.birthday)) {
                    if (!person.birthday.equals(pPerson.birthday)) {
                        person.birthday = pPerson.birthday;
                    }
                }
                if (BasicFunctions.isNotEmpty(pPerson.telephone)) {
                    if (BasicFunctions.isNotEmpty(person.telephone) && !person.telephone.equals(pPerson.telephone)) {
                        person.telephone = pPerson.telephone;
                    }
                }
                if (BasicFunctions.isNotEmpty(pPerson.telephone)) {
                    if (!person.telephone.equals(pPerson.telephone)) {
                        person.telephone = pPerson.telephone;
                    }
                }
                if (BasicFunctions.isNotEmpty(pPerson.cellphone)) {
                    if (!person.cellphone.equals(pPerson.cellphone)) {
                        person.cellphone = pPerson.cellphone;
                    }
                }
                if (BasicFunctions.isNotEmpty(pPerson.email)) {
                    if (!person.email.equals(pPerson.email)) {
                        person.email = pPerson.email;
                    }
                }
                if (BasicFunctions.isNotEmpty(address)) {
                    if (!person.address.equals(address)) {
                        person.address = address;
                    }
                }

                person.updatedBy = userAuth;
                person.updatedAt = LocalDateTime.now();
                person.persist();

                responses.status = 200;
                responses.data = person;
                responses.messages.add("Cadastro de Person updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = person;
            responses.messages.add("cannot update o cadastro Person.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deletePerson(@NotNull List<Long> pListPerson) {

        List<Person> persons;
        List<Person> personsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        persons = Person.list("id in ?1 and active = true", pListPerson);
        int count = persons.size();

        if (persons.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Persons not located or already excluded.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {

            persons.forEach((person) -> {

                person.updatedBy = userAuth;
                person.active = Boolean.FALSE;
                person.updatedAt = LocalDateTime.now();
                person.deletedAt = LocalDateTime.now();
                person.persist();
                personsAux.add(person);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = person;
                responses.messages.add("Person successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(personsAux);
                responses.messages.add(count + " Persons successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = person;
                responses.messages.add("Person not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(person);
                responses.messages.add("Persons not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivatePerson(@NotNull List<Long> pListPerson) {

        List<Person> persons;
        List<Person> personsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        persons = Person.list("id in ?1 and active = false", pListPerson);
        int count = persons.size();

        if (persons.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Persons not located or already reactivated.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {
            persons.forEach((person) -> {

                person.updatedBy = userAuth;
                person.active = Boolean.TRUE;
                person.updatedAt = LocalDateTime.now();
                person.deletedAt = LocalDateTime.now();
                person.persist();
                personsAux.add(person);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = person;
                responses.messages.add("Person reactived with sucessful!");
            } else {
                responses.datas = Collections.singletonList(personsAux);
                responses.messages.add(count + " Persons reactived with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = person;
                responses.messages.add("Person not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(person);
                responses.messages.add("Persons not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
