package app.quarkus.resources.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.Task;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static app.quarkus.filters.task.TaskFilters.makeTaskQueryStringByFilters;

@Path("/task")
public class TaskResources {

    @Inject
    app.quarkus.controller.appointments.PersonalActivityController controller;
    Task task;
    Responses responses;

    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response getById(@PathParam("id") Long pId) {
        task = Task.findById(pId);
        return Response.ok(task).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active) {
        query = "active = " + active;
        long count = Task.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response list(
            @QueryParam("taskDate") LocalDate taskDate,
            @QueryParam("startTime") LocalDate startTime,
            @QueryParam("endDate") LocalDate endDate,
            @QueryParam("personId") Long personId,
            @QueryParam("userId") String userId,
            @QueryParam("task") String task,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        String queryString = makeTaskQueryStringByFilters(taskDate, startTime, endDate,
                userId, task);
        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Task> tasks;
        tasks = Task.find(query);

        List<Task> taskFiltered = tasks.page(Page.of(pageIndex, pageSize)).list()
                .stream()
                .filter(x -> BasicFunctions.isEmpty(personId) || x.personalActivity.person.id.equals(personId))
                .collect(Collectors.toList());

        return Response.ok(taskFiltered).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(Task pTask, @Context @NotNull SecurityContext context) {
        try {

            return controller.addTask(pTask);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Task.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response update(Task pTask, @Context @NotNull SecurityContext context) {
        try {

            return controller.updateTask(pTask);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update the Task.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListIdTask, @Context @NotNull SecurityContext context) {
        try {

            return controller.deleteTask(pListIdTask);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdTask.size() <= 1) {
                responses.messages.add("cannot delete the Task.");
            } else {
                responses.messages.add("cannot delete the Tasks.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/reactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response reactivateList(List<Long> pListIdTask, @Context @NotNull SecurityContext context) {
        try {

            return controller.reactivateTask(pListIdTask);
        } catch (Exception e) {
            if (pListIdTask.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reactivate the Task.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reactivate the Tasks.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
