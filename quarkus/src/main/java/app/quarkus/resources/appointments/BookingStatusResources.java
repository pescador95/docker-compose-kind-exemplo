package app.quarkus.resources.appointments;

import app.core.model.DTO.Responses;
import app.quarkus.controller.appointments.BookingStatusController;
import app.quarkus.model.appointments.BookingStatus;
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

import static app.quarkus.filters.bookingStatus.StatusFilters.makeBookingStatusQueryStringByFilters;

@Path("/bookingStatus")
public class BookingStatusResources {

    @Inject
    BookingStatusController controller;
    BookingStatus bookingStatus;
    Responses responses;
    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response getById(@PathParam("id") Long pId) {
        bookingStatus = BookingStatus.findById(pId);
        return Response.ok(bookingStatus).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active) {
        query = "id > 0";
        long count = BookingStatus.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response list(
            @QueryParam("id") Long id,
            @QueryParam("status") String status,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder) {
        String queryString = makeBookingStatusQueryStringByFilters(id, status);
        query = "id > 0" + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<BookingStatus> statusAgendamento;
        statusAgendamento = BookingStatus.find(query);
        return Response.ok(statusAgendamento.page(Page.of(pageIndex, pageSize)).list()).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(BookingStatus pBookingStatus, @Context @NotNull SecurityContext context) {
        try {
            return controller.addStatusAgendamento(pBookingStatus);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Status de Appointments.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})

    public Response update(BookingStatus pBookingStatus, @Context @NotNull SecurityContext context) {
        try {
            return controller.updateStatusAgendamento(pBookingStatus);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update o Status de Appointments.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListIdStatusAgendamento, @Context @NotNull SecurityContext context) {
        try {
            return controller.deleteStatusAgendamento(pListIdStatusAgendamento);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListIdStatusAgendamento.size() <= 1) {
                responses.messages.add("cannot delete the Status de Appointments.");
            } else {
                responses.messages.add("cannot delete the Status de Agendamentos.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
