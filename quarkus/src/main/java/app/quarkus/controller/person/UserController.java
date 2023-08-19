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

    public Response addUser(@NotNull User pUsuario) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        if (BasicFunctions.isNotEmpty(pUsuario) && BasicFunctions.isNotEmpty(pUsuario.login)) {

            if (BasicFunctions.isNotEmpty(pUsuario) && BasicFunctions.isNotEmpty(pUsuario.login)) {

                user = User.find("login = ?1 and active = true", pUsuario.login.toLowerCase()).firstResult();
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

        if (BasicFunctions.isNotEmpty(pUsuario.organizations)) {
            pUsuario.organizations.forEach(organization -> organizationsId.add(organization.id));
            organizations = Organization.list("id in ?1", organizationsId);
        }

        if (BasicFunctions.isNotEmpty(pUsuario.serviceTypes)) {
            pUsuario.serviceTypes.forEach(serviceType -> tiposAgendamentosId.add(serviceType.id));
            serviceTypes = ServiceType.list("id in ?1", tiposAgendamentosId);
        }

        if (BasicFunctions.isNotEmpty(pUsuario.person) && pUsuario.person.isValid()) {
            person = Person.findById(pUsuario.person.id);
        }

        if (pUsuario.hasRole() && !pUsuario.bot) {
            pUsuario.role.forEach(role -> privilegioId.add(role.id));
            roles = Role.list("id in ?1", privilegioId);
            roles.removeIf(x -> x.id.equals(User.BOT));
        }

        if (BasicFunctions.isEmpty(user) && (!BasicFunctions.isEmpty(organizations))
                && (BasicFunctions.isNotEmpty(person))
                && (pUsuario.bot() || !(BasicFunctions.isEmpty(roles) && !pUsuario.bot()))) {
            user = new User();

            if (BasicFunctions.isNotEmpty(pUsuario.login)) {
                user.login = pUsuario.login.toLowerCase();
            }
            if (BasicFunctions.isNotEmpty(pUsuario.password)) {
                user.password = BcryptUtil.bcryptHash(pUsuario.password);
            }
            if (BasicFunctions.isNotEmpty(roles)) {
                user.role = new ArrayList<>();
                user.role.addAll(roles);
            }
            if (BasicFunctions.isNotEmpty(pUsuario.person)) {
                user.person = pUsuario.person;
                user.professionalName = user.person.name;
            }
            if (BasicFunctions.isNotEmpty(pUsuario.organizationDefault)) {
                user.organizationDefault = pUsuario.organizationDefault;
            }
            if (BasicFunctions.isNotEmpty(organizations)) {
                user.organizations = new ArrayList<>();
                user.organizations.addAll(organizations);
            }
            if (BasicFunctions.isNotEmpty(serviceTypes)) {
                user.serviceTypes = new ArrayList<>();
                user.serviceTypes.addAll(serviceTypes);
            }
            if (BasicFunctions.isNotEmpty(pUsuario.bot)) {
                user.bot = pUsuario.bot;
            } else {
                user.bot = Boolean.FALSE;
            }
            if (!user.bot) {
                roleDefault = new Role();
                roleDefault.setUsuario();
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

    public Response updateUser(@NotNull User pUsuario) {

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

        if (pUsuario.hasRole() && !pUsuario.bot()) {
            pUsuario.role.forEach(role -> privilegioId.add(role.id));
            roles = Role.list("id in ?1", privilegioId);
            roles.removeIf(x -> x.id.equals(User.BOT));
        }

        if (BasicFunctions.isNotEmpty(pUsuario.organizations)) {
            pUsuario.organizations.forEach(organization -> organizationsId.add(organization.id));
            organizations = Organization.list("id in ?1", organizationsId);
        }

        if (BasicFunctions.isNotEmpty(pUsuario.serviceTypes)) {
            pUsuario.serviceTypes.forEach(serviceType -> tiposAgendamentosId.add(serviceType.id));
            serviceTypes = ServiceType.list("id in ?1", tiposAgendamentosId);
        }

        try {

            if (BasicFunctions.isNotEmpty(pUsuario) && pUsuario.isValid()) {
                user = User.findById(pUsuario.id);
            }

            if (BasicFunctions.isEmpty(pUsuario.login) && BasicFunctions.isEmpty(pUsuario.password)
                    && BasicFunctions.isEmpty(pUsuario.role) && BasicFunctions.isEmpty(pUsuario.organizations)
                    && BasicFunctions.isEmpty(pUsuario.person)
                    && BasicFunctions.isEmpty(organizations) && BasicFunctions.isEmpty(serviceTypes)) {
                throw new BadRequestException("Enter data for update the User.");

            } else {

                if (BasicFunctions.isNotEmpty(pUsuario.login)) {
                    if (!user.login.equals(pUsuario.login.toLowerCase())) {
                        user.login = pUsuario.login.toLowerCase();
                    }
                }
                if (BasicFunctions.isNotEmpty(pUsuario.password)) {
                    if (BasicFunctions.isNotEmpty(user.password)
                            && !user.password.equals(BcryptUtil.bcryptHash(pUsuario.password))) {
                        user.password = BcryptUtil.bcryptHash(pUsuario.password);
                    }
                }
                if (pUsuario.hasRole()) {
                    if (!Objects.equals(user.role, pUsuario.role)) {
                        user.role = pUsuario.role;
                    }
                }
                if (BasicFunctions.isNotEmpty(pUsuario.person)) {
                    if (!Objects.equals(user.person, pUsuario.person)) {
                        user.person = pUsuario.person;
                        user.professionalName = user.person.name;
                    }
                }
                if (BasicFunctions.isNotEmpty(pUsuario.bot)) {
                    if (BasicFunctions.isNotEmpty(user.bot) && !Objects.equals(user.bot, pUsuario.bot)) {
                        user.bot = pUsuario.bot;
                    }
                }
                if (BasicFunctions.isNotEmpty(pUsuario.organizationDefault)) {
                    if (!Objects.equals(user.organizationDefault, pUsuario.organizationDefault)) {
                        user.organizationDefault = pUsuario.organizationDefault;
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
                if (BasicFunctions.isNotEmpty(pUsuario.bot)) {
                    user.bot = pUsuario.bot;
                } else {
                    user.bot = Boolean.FALSE;
                }
                if (!user.bot) {
                    roleDefault = new Role();
                    roleDefault.setUsuario();
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

    public Response deleteUser(@NotNull List<Long> pListIdUsuario) {

        List<User> users;
        List<User> usersAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        users = User.list("id in ?1 and active = true", pListIdUsuario);
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

    public Response reactivateUser(@NotNull List<Long> pListIdUsuario) {

        List<User> users;
        List<User> usersAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        users = User.list("id in ?1 and active = false", pListIdUsuario);
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
