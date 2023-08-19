package app.quarkus.controller.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.BookingStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class BookingStatusController {

    private BookingStatus BookingStatus = new BookingStatus();

    private Responses responses;

    public Response addStatusAgendamento(@NotNull BookingStatus pBookingStatus) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        if (BasicFunctions.isNotEmpty(pBookingStatus.status)) {
            BookingStatus = PanacheEntityBase.find("status = ?1 ", pBookingStatus.status).firstResult();
        }

        if (BasicFunctions.isEmpty(BookingStatus)) {
            BookingStatus = new BookingStatus();

            if (BasicFunctions.isNotEmpty(pBookingStatus.status)) {
                BookingStatus.status = pBookingStatus.status;
            } else {
                responses.messages.add("Input the Booking Status.");
            }
            if (!responses.hasMessages()) {
                BookingStatus.persist();
                responses.status = 201;
                responses.data = BookingStatus;
                responses.messages.add("Booking Status registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = BookingStatus;
            responses.messages.add("Booking Status already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateStatusAgendamento(@NotNull BookingStatus pBookingStatus) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pBookingStatus.isValid()) {
                BookingStatus = PanacheEntityBase.findById(pBookingStatus.id);
            }
            if (!pBookingStatus.isValid() && BasicFunctions.isEmpty(pBookingStatus.status)) {
                throw new BadRequestException("Enter data for update the registration of Booking Status.");
            } else {
                if (BasicFunctions.isNotEmpty(pBookingStatus.status)) {
                    BookingStatus.status = pBookingStatus.status;
                }
                BookingStatus.persist();
                responses.status = 200;
                responses.data = BookingStatus;
                responses.messages.add("Booking Status updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = BookingStatus;
            responses.messages.add("cannot update o cadastro de Booking Status.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteStatusAgendamento(@NotNull List<Long> pListIdStatusAgendamento) {

        List<BookingStatus> bookingStatuses;
        List<BookingStatus> bookingStatusAgendamentosAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        bookingStatuses = PanacheEntityBase.list("id in ?1", pListIdStatusAgendamento);
        int count = bookingStatuses.size();

        try {

            if (bookingStatuses.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Status dos Agendamentos not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            bookingStatuses.forEach((BookingStatus) -> {
                BookingStatus.delete();
                bookingStatusAgendamentosAux.add(BookingStatus);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = BookingStatus;
                responses.messages.add("Booking Status successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(bookingStatusAgendamentosAux);
                responses.messages.add(count + " Status dos Agendamentos successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = BookingStatus;
                responses.messages.add("Booking Status not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(bookingStatuses);
                responses.messages.add("Booking Status not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
