package app.quarkus.resources.person;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.controller.person.UserController;
import app.quarkus.model.person.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static app.quarkus.filters.person.UserFilters.makeUserQueryStringByFilters;

@Path("/user")
public class UserResources {

    @Inject
    UserController controller;
    Responses responses;
    User userAuth;
    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response getById(@PathParam("id") Long pId) {
        User user = User.findById(pId);
        return Response.ok(user).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active) {
        query = "active = " + active;
        long count = User.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response list(
            @QueryParam("id") Long id,
            @QueryParam("login") String login,
            @QueryParam("personId") Long personId,
            @QueryParam("organizationDefaultId") Long organizationDefaultId,
            @QueryParam("roleId") List<Long> roleId,
            @QueryParam("professionalName") String professionalName,
            @QueryParam("user") String user,
            @QueryParam("organizationId") List<Long> organizationId,
            @QueryParam("serviceType") List<Long> serviceType,
            @QueryParam("bot") Boolean bot,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder,
            @javax.ws.rs.core.Context @NotNull SecurityContext context) {

        userAuth = Context.getContextUser(context);

        String queryString = makeUserQueryStringByFilters(id, login, personId, organizationDefaultId,
                professionalName, user, bot);

        query = "active = " + active + " " + queryString + " " + "order by " + strgOrder + " " + sortQuery;
        PanacheQuery<User> users;
        users = User.find(query);

        List<User> usersFiltered = users.page(Page.of(pageIndex, pageSize)).list()
                .stream()
                .filter(x -> (BasicFunctions.isEmpty(organizationId)
                        || new HashSet<>(x.organizations.stream().map(org -> org.id).collect(Collectors.toList()))
                                .containsAll(organizationId))
                        && (BasicFunctions.isEmpty(serviceType) || new HashSet<>(
                                x.serviceTypes.stream().map(t -> t.id).collect(Collectors.toList()))
                                .containsAll(serviceType))
                        && (BasicFunctions.isEmpty(roleId) || new HashSet<>(
                                x.role.stream().map(role -> role.id).collect(Collectors.toList()))
                                .containsAll(roleId)))
                .collect(Collectors.toList());

        return Response.ok(usersFiltered).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response add(User pUser, @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {

            return controller.addUser(pUser);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the User.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response update(User pUser, @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {

            return controller.updateUser(pUser);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update the User.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response deleteList(List<Long> pListIdUser, @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {

            return controller.deleteUser(pListIdUser);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdUser.size() <= 1) {
                responses.messages.add("cannot delete the User.");
            } else {
                responses.messages.add("cannot delete the Users.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/reactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response reactivateList(List<Long> pListIdUser,
            @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {

            return controller.reactivateUser(pListIdUser);
        } catch (Exception e) {
            if (pListIdUser.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reactivate the User.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reactivate the Users.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
