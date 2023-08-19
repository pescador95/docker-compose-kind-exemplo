package app.quarkus.resources.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.quarkus.controller.appointments.ServiceTypeController;
import app.quarkus.model.appointments.ServiceType;
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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static app.quarkus.filters.serviceType.ServiceTypeFilters.makeServiceTypeQueryStringByFilters;

@Path("/serviceType")
public class ServiceTypeResources {

    @Inject
    ServiceTypeController controller;
    ServiceType serviceType;
    Responses responses;
    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response getById(@PathParam("id") Long pId) {
        serviceType = ServiceType.findById(pId);
        return Response.ok(serviceType).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response count() {
        query = "id > 0";
        long count = ServiceType.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response list(
            @QueryParam("id") Long id,
            @QueryParam("serviceType") String serviceType,
            @QueryParam("organizationId") List<Long> organizationId,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder) {
        String queryString = makeServiceTypeQueryStringByFilters(id, serviceType);
        query = "id > 0" + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<ServiceType> tipoAgendamentos;
        tipoAgendamentos = ServiceType.find(query);

        List<ServiceType> tiposAgendamentosFiltered = tipoAgendamentos.page(Page.of(pageIndex, pageSize)).list()
                .stream()
                .filter(x -> BasicFunctions.isValid(organizationId)
                        || new HashSet<>(x.organizations.stream().map(org -> org.id)
                        .collect(Collectors.toList()))
                        .containsAll(organizationId))
                .collect(Collectors.toList());

        return Response.ok(tiposAgendamentosFiltered).status(200).build();
    }

    @GET
    @Path("/bot")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response listByScheduler(@QueryParam("organizations") @NotNull List<Long> organizations,
                                    @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
                                    @QueryParam("page") @DefaultValue("0") int pageIndex,
                                    @QueryParam("size") @DefaultValue("20") int pageSize,
                                    @QueryParam("active") @DefaultValue("true") Boolean active,
                                    @QueryParam("strgOrder") @DefaultValue("id") String strgOrder) {
        query = "id > 0 " + " " + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<ServiceType> serviceType;
        serviceType = ServiceType.find(query);

        List<ServiceType> tiposAgendamentosFiltered = serviceType.page(Page.of(pageIndex, pageSize)).list()
                .stream()
                .filter(x -> BasicFunctions.isEmpty(organizations)
                        || new HashSet<>(x.organizations.stream().map(org -> org.id)
                        .collect(Collectors.toList()))
                        .containsAll(organizations))
                .collect(Collectors.toList());

        return Response.ok(tiposAgendamentosFiltered).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(ServiceType pTipoAgendamento, @Context @NotNull SecurityContext context) {
        try {
            return controller.addTipoAgendamento(pTipoAgendamento);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Appointment.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response update(ServiceType pTipoAgendamento, @Context @NotNull SecurityContext context) {
        try {
            return controller.updateTipoAgendamento(pTipoAgendamento);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update the Appointments.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListIdTipoAgendamento, @Context @NotNull SecurityContext context) {
        try {
            return controller.deleteTipoAgendamento(pListIdTipoAgendamento);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdTipoAgendamento.size() <= 1) {
                responses.messages.add("cannot delete the Appointments.");
            } else {
                responses.messages.add("cannot delete the Appointments.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
