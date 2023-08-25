package app.quarkus.resources.person;

import app.core.model.DTO.Responses;
import app.quarkus.controller.person.PersonalActivityController;
import app.quarkus.model.person.PersonalActivity;
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
import java.util.stream.Collectors;

import static app.quarkus.filters.person.PersonalActivityFilters.makePersonalActivityQueryStringByFilters;

@Path("/personalActivity")
public class PersonalActivityResources {

    @Inject
    PersonalActivityController controller;
    PersonalActivity personalActivity;
    Responses responses;

    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response getById(@PathParam("id") Long pId) {
        personalActivity = PersonalActivity.findById(pId);
        return Response.ok(personalActivity).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active) {
        query = "active = " + active;
        long count = PersonalActivity.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response list(
            @QueryParam("id") Long id,
            @QueryParam("queixaPrincipal") String queixaPrincipal,
            @QueryParam("medicamentos") String medicamentos,
            @QueryParam("diagnosticoClinico") String diagnosticoClinico,
            @QueryParam("comorbidades") String comorbidades,
            @QueryParam("ocupacao") String ocupacao,
            @QueryParam("responsibleContato") String responsibleContato,
            @QueryParam("personName") String personName,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        String queryString = makePersonalActivityQueryStringByFilters(id, queixaPrincipal, medicamentos,
                diagnosticoClinico, comorbidades, ocupacao, responsibleContato, personName);
        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<PersonalActivity> personalActivity;
        personalActivity = PersonalActivity.find(query);
        List<PersonalActivity> historicoPersonFiltered = personalActivity.page(Page.of(pageIndex, pageSize)).list()
                .stream().filter(c -> (c.active = active))
                .collect(Collectors.toList());

        return Response.ok(historicoPersonFiltered).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response add(PersonalActivity pPersonalActivity, @Context @NotNull SecurityContext context) {
        try {

            return controller.addPersonalActivity(pPersonalActivity);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the PersonalActivity.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response update(PersonalActivity pPersonalActivity, @Context @NotNull SecurityContext context) {
        try {

            return controller.updatePersonalActivity(pPersonalActivity);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update o PersonalActivity.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response deleteList(List<Long> pListIdPersonalActivity, @Context @NotNull SecurityContext context) {
        try {

            return controller.deletePersonalActivity(pListIdPersonalActivity);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdPersonalActivity.size() <= 1) {
                responses.messages.add("cannot delete the PersonalActivity.");
            } else {
                responses.messages.add("cannot delete the Clientes.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/reactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response reactivateList(List<Long> pListIdCliente, @Context @NotNull SecurityContext context) {
        try {

            return controller.reactivatePersonalActivity(pListIdCliente);
        } catch (Exception e) {
            if (pListIdCliente.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar o PersonalActivity.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar os Clientes.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
