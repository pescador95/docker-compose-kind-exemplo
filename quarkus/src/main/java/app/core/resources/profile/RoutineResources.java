package app.core.resources.profile;

import app.core.controller.profile.RoutineController;
import app.core.model.DTO.Responses;
import app.core.model.profile.Routine;
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
import java.util.List;

import static app.core.filters.profile.RoutineFilters.makeRoutineQueryStringByFilters;

@Path("/routine")
public class RoutineResources {

    @Inject
    RoutineController controller;
    Routine routine;
    Responses responses;
    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response getById(@PathParam("id") Long pId) {
        routine = Routine.findById(pId);
        return Response.ok(routine).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active,
                          @Context @NotNull SecurityContext context) {
        query = "id > 0 ";
        long count = Routine.count(query);
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
            @QueryParam("icon") String icon,
            @QueryParam("path") String path,
            @QueryParam("title") String title,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        String queryString = makeRoutineQueryStringByFilters(id, name, icon, path, title);
        query = "id > 0 " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Routine> routines;
        routines = Routine.find(query);

        List<Routine> routineFiltered = routines.page(Page.of(pageIndex, pageSize)).list();

        return Response.ok(routineFiltered).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(Routine pRoutine, @Context @NotNull SecurityContext context) {
        try {

            return controller.addRoutine(pRoutine);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Routine.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response update(Routine pRoutine, @Context @NotNull SecurityContext context) {
        try {

            return controller.updateRoutine(pRoutine);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update a Routine.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListRoutine, @Context @NotNull SecurityContext context) {
        try {

            return controller.deleteRoutine(pListRoutine);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListRoutine.size() <= 1) {
                responses.messages.add("cannot delete the Routine.");
            } else {
                responses.messages.add("cannot delete the Routines.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

}
