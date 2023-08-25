package app.quarkus.controller.person;

import app.core.model.DTO.Responses;
import app.core.model.auth.Role;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.appointments.ServiceType;
import app.quarkus.model.organization.Organization;
import app.quarkus.model.person.Person;
import app.quarkus.model.person.User;
import io.quarkus.elytron.security.common.BcryptUtil;
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
import java.util.Objects;

@ApplicationScoped
@Transactional
public class UserController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private User user;
    private Responses responses;
    private User userAuth;
    private Person person;

    public Response addUser(@NotNull User pUser) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        if (BasicFunctions.isNotEmpty(pUser) && BasicFunctions.isNotEmpty(pUser.login)) {

            if (BasicFunctions.isNotEmpty(pUser) && BasicFunctions.isNotEmpty(pUser.login)) {

                user = User.find("login = ?1 and active = true", pUser.login.toLowerCase()).firstResult();
            }
        } else {
            responses.messages.add("Por favor, verifique o login!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        List<Organization> organizations = new ArrayList<>();

        List<Long> organizationsId = new ArrayList<>();

        List<ServiceType> serviceTypes = new ArrayList<>();

        List<Long> tiposAgendamentosId = new ArrayList<>();

        List<Role> roles = new ArrayList<>();

        List<Long> privilegioId = new ArrayList<>();

        Role roleDefault;

        if (BasicFunctions.isNotEmpty(pUser.organizations)) {
            pUser.organizations.forEach(organization -> organizationsId.add(organization.id));
            organizations = Organization.list("id in ?1", organizationsId);
        }

        if (BasicFunctions.isNotEmpty(pUser.serviceTypes)) {
            pUser.serviceTypes.forEach(serviceType -> tiposAgendamentosId.add(serviceType.id));
            serviceTypes = ServiceType.list("id in ?1", tiposAgendamentosId);
        }

        if (BasicFunctions.isNotEmpty(pUser.person) && pUser.person.isValid()) {
            person = Person.findById(pUser.person.id);
        }

        if (pUser.hasRole() && !pUser.bot) {
            pUser.role.forEach(role -> privilegioId.add(role.id));
            roles = Role.list("id in ?1", privilegioId);
            roles.removeIf(x -> x.id.equals(User.BOT));
        }

        if (BasicFunctions.isEmpty(user) && (!BasicFunctions.isEmpty(organizations))
                && (BasicFunctions.isNotEmpty(person))
                && (pUser.bot() || !(BasicFunctions.isEmpty(roles) && !pUser.bot()))) {
            user = new User();

            if (BasicFunctions.isNotEmpty(pUser.login)) {
                user.login = pUser.login.toLowerCase();
            }
            if (BasicFunctions.isNotEmpty(pUser.password)) {
                user.password = BcryptUtil.bcryptHash(pUser.password);
            }
            if (BasicFunctions.isNotEmpty(roles)) {
                user.role = new ArrayList<>();
                user.role.addAll(roles);
            }
            if (BasicFunctions.isNotEmpty(pUser.person)) {
                user.person = pUser.person;
                user.professionalName = user.person.name;
            }
            if (BasicFunctions.isNotEmpty(pUser.organizationDefault)) {
                user.organizationDefault = pUser.organizationDefault;
            }
            if (BasicFunctions.isNotEmpty(organizations)) {
                user.organizations = new ArrayList<>();
                user.organizations.addAll(organizations);
            }
            if (BasicFunctions.isNotEmpty(serviceTypes)) {
                user.serviceTypes = new ArrayList<>();
                user.serviceTypes.addAll(serviceTypes);
            }
            if (BasicFunctions.isNotEmpty(pUser.bot)) {
                user.bot = pUser.bot;
            } else {
                user.bot = Boolean.FALSE;
            }
            if (!user.bot) {
                roleDefault = new Role();
                roleDefault.setUser();
                user.role.add(roleDefault);
            } else {
                roleDefault = new Role();
                roleDefault.setBot();
                user.role.add(roleDefault);
            }
            if (!responses.hasMessages()) {
                user.user = userAuth.person.name;
                user.updatedBy = userAuth.person.name;
                user.active = Boolean.TRUE;
                user.changePassword = Boolean.FALSE;
                user.updatedAt = LocalDateTime.now();
                user.persist();

                responses.status = 201;
                responses.data = user;
                responses.messages.add("User registered successfully!");

            } else {
                responses.messages.add("Please check the required information!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = user;
            responses.messages.add("Check the required information!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateUser(@NotNull User pUser) {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        List<Organization> organizations = new ArrayList<>();

        List<Long> organizationsId = new ArrayList<>();

        List<Role> roles = new ArrayList<>();

        List<Long> privilegioId = new ArrayList<>();

        List<ServiceType> serviceTypes = new ArrayList<>();

        List<Long> tiposAgendamentosId = new ArrayList<>();
        Role roleDefault;

        if (pUser.hasRole() && !pUser.bot()) {
            pUser.role.forEach(role -> privilegioId.add(role.id));
            roles = Role.list("id in ?1", privilegioId);
            roles.removeIf(x -> x.id.equals(User.BOT));
        }

        if (BasicFunctions.isNotEmpty(pUser.organizations)) {
            pUser.organizations.forEach(organization -> organizationsId.add(organization.id));
            organizations = Organization.list("id in ?1", organizationsId);
        }

        if (BasicFunctions.isNotEmpty(pUser.serviceTypes)) {
            pUser.serviceTypes.forEach(serviceType -> tiposAgendamentosId.add(serviceType.id));
            serviceTypes = ServiceType.list("id in ?1", tiposAgendamentosId);
        }

        try {

            if (BasicFunctions.isNotEmpty(pUser) && pUser.isValid()) {
                user = User.findById(pUser.id);
            }

            if (BasicFunctions.isEmpty(pUser.login) && BasicFunctions.isEmpty(pUser.password)
                    && BasicFunctions.isEmpty(pUser.role) && BasicFunctions.isEmpty(pUser.organizations)
                    && BasicFunctions.isEmpty(pUser.person)
                    && BasicFunctions.isEmpty(organizations) && BasicFunctions.isEmpty(serviceTypes)) {
                throw new BadRequestException("Enter data for update the User.");

            } else {

                if (BasicFunctions.isNotEmpty(pUser.login)) {
                    if (!user.login.equals(pUser.login.toLowerCase())) {
                        user.login = pUser.login.toLowerCase();
                    }
                }
                if (BasicFunctions.isNotEmpty(pUser.password)) {
                    if (BasicFunctions.isNotEmpty(user.password)
                            && !user.password.equals(BcryptUtil.bcryptHash(pUser.password))) {
                        user.password = BcryptUtil.bcryptHash(pUser.password);
                    }
                }
                if (pUser.hasRole()) {
                    if (!Objects.equals(user.role, pUser.role)) {
                        user.role = pUser.role;
                    }
                }
                if (BasicFunctions.isNotEmpty(pUser.person)) {
                    if (!Objects.equals(user.person, pUser.person)) {
                        user.person = pUser.person;
                        user.professionalName = user.person.name;
                    }
                }
                if (BasicFunctions.isNotEmpty(pUser.bot)) {
                    if (BasicFunctions.isNotEmpty(user.bot) && !Objects.equals(user.bot, pUser.bot)) {
                        user.bot = pUser.bot;
                    }
                }
                if (BasicFunctions.isNotEmpty(pUser.organizationDefault)) {
                    if (!Objects.equals(user.organizationDefault, pUser.organizationDefault)) {
                        user.organizationDefault = pUser.organizationDefault;
                    }
                }
                if (BasicFunctions.isNotEmpty(organizations)) {
                    user.organizations = new ArrayList<>();
                    user.organizations.addAll(organizations);
                }
                if (BasicFunctions.isNotEmpty(roles)) {
                    user.role = new ArrayList<>();
                    user.role.addAll(roles);
                }
                if (BasicFunctions.isNotEmpty(serviceTypes)) {
                    user.serviceTypes = new ArrayList<>();
                    user.serviceTypes.addAll(serviceTypes);
                }
                if (BasicFunctions.isNotEmpty(pUser.bot)) {
                    user.bot = pUser.bot;
                } else {
                    user.bot = Boolean.FALSE;
                }
                if (!user.bot) {
                    roleDefault = new Role();
                    roleDefault.setUser();
                    user.role.add(roleDefault);
                } else {
                    roleDefault = new Role();
                    roleDefault.setBot();
                    user.role.add(roleDefault);
                }
                user.updatedBy = userAuth.login;
                user.updatedAt = LocalDateTime.now();

                user.persist();

                responses.status = 200;
                responses.data = user;
                responses.messages.add("User updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = user;
            responses.messages.add("cannot update the User.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteUser(@NotNull List<Long> pListIdUser) {

        List<User> users;
        List<User> usersAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        users = User.list("id in ?1 and active = true", pListIdUser);
        int count = users.size();

        try {

            if (users.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Users not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            users.forEach((user) -> {

                user.updatedBy = userAuth.user;
                user.active = Boolean.FALSE;
                user.updatedAt = LocalDateTime.now();
                user.deletedAt = LocalDateTime.now();
                user.persist();
                usersAux.add(user);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = user;
                responses.messages.add("User successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(usersAux);
                responses.messages.add(count + " Users successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = user;
                responses.messages.add("User not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(users);
                responses.messages.add("Users not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivateUser(@NotNull List<Long> pListIdUser) {

        List<User> users;
        List<User> usersAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        users = User.list("id in ?1 and active = false", pListIdUser);
        int count = users.size();

        if (users.isEmpty()) {
            responses.status = 400;
            responses.messages.add("Users not located or already reactivated.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }

        try {

            users.forEach((user) -> {

                user.updatedBy = userAuth.user;
                user.active = Boolean.TRUE;
                user.updatedAt = LocalDateTime.now();
                user.deletedAt = LocalDateTime.now();
                user.persist();
                usersAux.add(user);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = user;
                responses.messages.add("User reactived with sucessful!");
            } else {
                responses.datas = Collections.singletonList(usersAux);
                responses.messages.add(count + " Users reactived with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = user;
                responses.messages.add("User not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(users);
                responses.messages.add("Users not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
