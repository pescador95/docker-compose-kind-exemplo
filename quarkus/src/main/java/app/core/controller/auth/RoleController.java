package app.core.controller.auth;

import app.core.model.DTO.Responses;
import app.core.model.auth.Role;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.person.User;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class RoleController {
    User userAuth;
    @javax.ws.rs.core.Context
    SecurityContext context;
    private Role role;
    private Responses responses;

    public Response addRole(@NotNull Role pRole) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        userAuth = Context.getContextUser(context);

        if (pRole.hasPrivilegio()) {
            role = Role.find("role = ?1", pRole.role).firstResult();
        } else {
            responses.messages.add("The field 'role' is required!");
        }

        if (BasicFunctions.isEmpty(role)) {

            role = new Role();
            role.role = pRole.role;
            if (BasicFunctions.isNotEmpty(pRole.admin)) {
                role.admin = pRole.admin;
            } else {
                role.admin = Boolean.FALSE;
            }

            if (!responses.hasMessages()) {
                role.persist();
                responses.status = 201;
                responses.data = role;
                responses.messages.add("Role registered successfully!");

            } else {
                responses.messages.add("Please check the required information!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = role;
            responses.messages.add("Check the required information!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateRole(@NotNull Role pRole) {

        userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pRole.isValid()) {
                role = Role.findById(pRole.id);
            }

            if (!pRole.hasPrivilegio()) {
                throw new BadRequestException("Informe os name do privilégio para update o mesmo.");

            } else {
                role.role = pRole.role;
                if (BasicFunctions.isNotEmpty(pRole.admin)) {
                    role.admin = pRole.admin;
                }
                role.persist();
                responses.status = 200;
                responses.data = role;
                responses.messages.add("Role updated successfully!");
                return Response.ok(responses).status(Response.Status.ACCEPTED).build();
            }
        } catch (Exception e) {
            responses.status = 400;
            responses.data = role;
            responses.messages.add("cannot update o Role.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteRole(@NotNull List<Long> pListIdRole) {

        List<Role> roles;
        List<Role> rolesAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        roles = User.list("id in ?1 and active = true", pListIdRole);
        int count = roles.size();

        try {

            if (roles.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Roles not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            roles.forEach((role) -> {
                rolesAux.add(role);
                role.delete();
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = role;
                responses.messages.add("Role successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(rolesAux);
                responses.messages.add(count + " Roles successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = role;
                responses.messages.add("Role not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(roles);
                responses.messages.add("Roles not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
