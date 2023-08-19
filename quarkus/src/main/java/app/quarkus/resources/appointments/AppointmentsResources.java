package app.quarkus.resources.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.Context;
import app.quarkus.DTO.AppointmentsDTO;
import app.quarkus.controller.DTO.AppointmentsDTOController;
import app.quarkus.model.appointments.Appointments;
import app.quarkus.model.person.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static app.quarkus.filters.appointments.AppointmentsFilters.makeAppointmentsQueryStringByFilters;

@Path("/Appointments")
public class AppointmentsResources {

    @Inject
    app.quarkus.controller.appointments.AppointmentsController controller;

    Appointments Appointments;

    Responses responses;
    private User userAuth;

    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response getById(@PathParam("id") Long pId) {
        Appointments = PanacheEntityBase.findById(pId);
        return Response.ok(Appointments).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active) {
        query = "active = " + active;
        long count = PanacheEntityBase.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response list(
            @QueryParam("appointmentDate") LocalDate appointmentDate,
            @QueryParam("startDate") LocalDate startDate,
            @QueryParam("endDate") LocalDate endDate,
            @QueryParam("appointmentTime") LocalTime appointmentTime,
            @QueryParam("startTime") LocalTime startTime,
            @QueryParam("endTime") LocalTime endTime,
            @QueryParam("personId") Long personId,
            @QueryParam("personName") String personName,
            @QueryParam("professionalName") String professionalName,
            @QueryParam("idStatus") @DefaultValue("1") Long idStatus,
            @QueryParam("organizationId") Long organizationId,
            @QueryParam("serviceType") Long serviceType,
            @QueryParam("professionalId") Long professionalId,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder,
            @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        userAuth = Context.getContextUser(context);

        String queryString = makeAppointmentsQueryStringByFilters(appointmentDate, startDate, endDate,
                appointmentTime, startTime, endTime, personId, personName, professionalName, idStatus,
                organizationId, serviceType, professionalId);

        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Appointments> appointments;
        appointments = PanacheEntityBase.find(query);
        List<Appointments> appointmentssFiltered = appointments.page(Page.of(pageIndex, pageSize)).list().stream()
                .filter(c -> (userAuth.organizations.contains(c.organizationAppointments)))
                .collect(Collectors.toList());

        List<AppointmentsDTO> appointmentssDTO = AppointmentsDTOController
                .makeListAgendamentoDTO(appointmentssFiltered);

        return Response.ok(appointmentssDTO).status(200).build();
    }

    @GET
    @Path("/mobile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response listMobile(@QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
                               @QueryParam("page") @DefaultValue("0") int pageIndex,
                               @QueryParam("appointmentDate") @DefaultValue("1970-01-01") LocalDate appointmentDate,
                               @QueryParam("appointmentTime") @DefaultValue("00:00") LocalTime appointmentTime,
                               @QueryParam("size") @DefaultValue("20") int pageSize,
                               @QueryParam("active") @DefaultValue("true") Boolean active,

                               @QueryParam("strgOrder") @DefaultValue("id") String strgOrder) {
        List<Appointments> appointments = PanacheEntityBase.list("id > 0");

        List<AppointmentsDTO> appointmentssDTO = AppointmentsDTOController.makeListAgendamentoDTO(appointments);

        return Response.ok(appointmentssDTO).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(Appointments pAppointments, @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {
            return controller.addAgendamento(pAppointments);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Appointments.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response update(Appointments pAppointments) {
        try {
            return controller.updateAgendamento(pAppointments);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update o Appointments.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListIdAgendamento,
                               @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {

            return controller.deleteAgendamento(pListIdAgendamento);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdAgendamento.size() <= 1) {
                responses.messages.add("cannot delete the Appointments.");
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
    @RolesAllowed({"user"})
    public Response reactivateList(List<Long> pListIdAgendamento,
                                   @javax.ws.rs.core.Context @NotNull SecurityContext context) {
        try {

            return controller.reactivateAgendamento(pListIdAgendamento);
        } catch (Exception e) {
            if (pListIdAgendamento.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar o Appointments.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar os Organizations.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
