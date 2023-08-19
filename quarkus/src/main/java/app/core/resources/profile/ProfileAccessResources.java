package app.core.resources.profile;

import app.core.controller.profile.ProfileAccessController;
import app.core.model.DTO.Responses;
import app.core.model.profile.ProfileAccess;
import app.core.utils.BasicFunctions;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static app.core.filters.profile.ProfileAccessFilters.makeProfileAccessQueryStringByFilters;

@Path("/profileAccess")
public class ProfileAccessResources {

    @Inject
    ProfileAccessController controller;
    ProfileAccess profileAccess;
    Responses responses;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response getById(@PathParam("id") Long pId) {
        profileAccess = ProfileAccess.findById(pId);
        return Response.ok(profileAccess).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response count() {
        String query = "id > 0 ";
        Long count = ProfileAccess.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response list(
            @QueryParam("id") Long id,
            @QueryParam("name") String name,
            @QueryParam("create") Boolean create,
            @QueryParam("read") Boolean read,
            @QueryParam("update") Boolean update,
            @QueryParam("delete") Boolean delete,
            @QueryParam("userId") Long userId,
            @QueryParam("routineId") List<Long> routineId,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {

        String queryString = makeProfileAccessQueryStringByFilters(id, name, create, read, update, delete, userId);
        String query = "id > 0" + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<ProfileAccess> profileAccess;
        profileAccess = ProfileAccess.find(query);

        List<ProfileAccess> profileAccessFiltered = profileAccess.page(Page.of(pageIndex, pageSize)).list()
                .stream()
                .filter(x -> BasicFunctions.isEmpty(routineId) || new HashSet<>(x.routines.stream().map(org -> org.id)
                        .collect(Collectors.toList()))
                        .containsAll(routineId))
                .collect(Collectors.toList());

        return Response.ok(profileAccessFiltered).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(ProfileAccess pProfileAccess, @Context @NotNull SecurityContext context) {
        try {

            return controller.addProfileAccess(pProfileAccess);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Profile Access.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response update(ProfileAccess pProfileAccess, @Context @NotNull SecurityContext context) {
        try {

            return controller.updateProfileAccess(pProfileAccess);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update o Profile Access.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListProfileAccess, @Context @NotNull SecurityContext context) {
        try {

            return controller.deleteProfileAccess(pListProfileAccess);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListProfileAccess.size() <= 1) {
                responses.messages.add("cannot delete the Profile Access.");
            } else {
                responses.messages.add("cannot delete the Profile Access.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

}
