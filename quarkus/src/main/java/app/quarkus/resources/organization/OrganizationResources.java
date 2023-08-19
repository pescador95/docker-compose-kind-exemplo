package app.quarkus.resources.organization;

import app.core.model.DTO.Responses;
import app.quarkus.controller.organization.OrganizationController;
import app.quarkus.model.organization.Organization;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static app.quarkus.filters.organization.OrganizationFilters.makeOrganizationQueryStringByFilters;

@Path("/organization")
public class OrganizationResources {

    @Inject
    OrganizationController controller;
    Organization organization;
    Responses responses;

    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})

    public Response getById(@PathParam("id") Long pId) {
        organization = Organization.findById(pId);
        return Response.ok(organization).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active) {

        query = "active = " + active;
        long count = Organization.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})
    public Response list(
            @QueryParam("name") String name,
            @QueryParam("cnpj") String cnpj,
            @QueryParam("telephone") String telephone,
            @QueryParam("cellphone") String cellphone,
            @QueryParam("email") String email,
            @QueryParam("addressId") Long addressId,
            @QueryParam("tipoAgendamento_Id") Long tipoAgendamento_Id,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder) {
        String queryString = makeOrganizationQueryStringByFilters(name, cnpj, telephone, cellphone, email, addressId,
                tipoAgendamento_Id);
        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Organization> organization;
        organization = Organization.find(query);
        return Response.ok(organization.page(Page.of(pageIndex, pageSize)).list()).status(200).build();
    }

    @GET
    @Path("/bot/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response listByBot(@QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
                              @QueryParam("page") @DefaultValue("0") int pageIndex,
                              @QueryParam("size") @DefaultValue("20") int pageSize,
                              @QueryParam("active") @DefaultValue("true") Boolean active,
                              @QueryParam("strgOrder") @DefaultValue("id") String strgOrder) {
        String queryString = ""; // makeOrganizationQueryStringByFilters();
        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Organization> organization;
        organization = Organization.find(query);
        return Response.ok(organization.page(Page.of(pageIndex, pageSize)).list()).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})
    public Response add(Organization pOrganization, @Context @NotNull SecurityContext context) {
        try {

            return controller.addOrganization(pOrganization);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Organization.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})

    public Response update(Organization pOrganization, @Context @NotNull SecurityContext context) {
        try {

            return controller.updateOrganization(pOrganization);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update a Organization.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})
    public Response deleteList(List<Long> pListIdOrganization, @Context @NotNull SecurityContext context) {
        try {

            return controller.deleteOrganization(pListIdOrganization);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdOrganization.size() <= 1) {
                responses.messages.add("cannot delete the Organization.");
            } else {
                responses.messages.add("cannot delete the Organizations.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/reactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user", "bot"})
    public Response reactivateList(List<Long> pListIdOrganization, @Context @NotNull SecurityContext context) {
        try {

            return controller.reactivateOrganization(pListIdOrganization);
        } catch (Exception e) {
            if (pListIdOrganization.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar a Organization.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar as Organizations.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
