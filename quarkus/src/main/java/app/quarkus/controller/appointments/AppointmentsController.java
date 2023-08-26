package app.quarkus.controller.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.controller.person.UserController;
import app.quarkus.model.appointments.Appointments;
import app.quarkus.model.appointments.BookingStatus;
import app.quarkus.model.organization.Organization;
import app.quarkus.model.person.Person;
import app.quarkus.model.person.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class AppointmentsController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    @Inject
    UserController userController;
    @Inject
    app.quarkus.controller.appointments.AppointmentsController appointmentsController;
    private Appointments Appointments;
    private User userAuth;
    private User profissional;
    private Person person;
    private Organization organization;

    public Response addAgendamento(@NotNull Appointments pAppointments) {

        Responses responses;
        responses = new Responses();
        responses.messages = new ArrayList<>();
        organization = new Organization();
        userAuth = Context.getContextUser(context);
        Appointments = PanacheEntityBase.find("personAppointments = ?1 and appointmentDate = ?2 and active = true",
                pAppointments.personAppointments, pAppointments.appointmentDate).firstResult();

        if (BasicFunctions.isNotEmpty(pAppointments.professionalAppointments)
                && pAppointments.professionalAppointments.isValid()) {
            profissional = User.findById(pAppointments.professionalAppointments.id);
        }

        if (BasicFunctions.isNotEmpty(pAppointments.personAppointments)
                && pAppointments.personAppointments.isValid()) {
            person = Person.findById(pAppointments.personAppointments.id);
        }
        if (BasicFunctions.isNotEmpty(pAppointments.organizationAppointments)
                && pAppointments.organizationAppointments.isValid()) {
            organization = Organization.findById(pAppointments.organizationAppointments.id);
        }

        if (BasicFunctions.isEmpty(Appointments)) {
            Appointments = new Appointments();

            if (BasicFunctions.isValid(pAppointments.appointmentDate)) {
                Appointments.appointmentDate = pAppointments.appointmentDate;
            } else {
                responses.messages.add("Please, insert the correct Date!");
            }
            if (BasicFunctions.isValid(pAppointments.appointmentTime)) {
                Appointments.appointmentTime = pAppointments.appointmentTime;
            } else {
                responses.messages.add("Please, insert the correct Time!");
            }
            if (BasicFunctions.isNotEmpty(profissional)) {
                Appointments.professionalAppointments = profissional;
                Appointments.professionalName = profissional.person.name;
            } else {
                Appointments.professionalAppointments = userAuth;
                Appointments.professionalName = userAuth.person.name;
            }
            if (BasicFunctions.isNotEmpty(person)) {
                Appointments.personAppointments = person;
                Appointments.personName = person.name;
            }
            if (BasicFunctions.isNotEmpty(organization)) {
                Appointments.organizationAppointments = organization;
            } else {
                responses.messages.add("Please, select the Organization!");
            }
            if (BasicFunctions.isNotEmpty(pAppointments.preference)) {
                Appointments.preference = pAppointments.preference;
            } else {
                Appointments.preference = Boolean.FALSE;
            }
            if (!responses.hasMessages()) {
                Appointments.bookingStatus = BookingStatus.findById(pAppointments.serviceType.id);
                Appointments.user = userAuth;
                Appointments.updatedBy = userAuth;
                Appointments.updatedAt = LocalDateTime.now();
                Appointments.persist();
                responses.status = 201;
                responses.data = Appointments;
                responses.messages.add("Appointments registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = Appointments;
            responses.messages.add("Appointments already done!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateAgendamento(@NotNull Appointments pAppointments) {

        Responses responses;
        User userAuth = Context.getContextUser(context);

        responses = new Responses();
        responses.messages = new ArrayList<>();
        organization = new Organization();

        if (pAppointments.isValid()) {
            Appointments = PanacheEntityBase.find("id = ?1 and active = true", pAppointments.id).firstResult();
        }

        if (BasicFunctions.isNotEmpty(pAppointments.professionalAppointments)
                && pAppointments.professionalAppointments.isValid()) {
            profissional = User.findById(pAppointments.professionalAppointments.id);
        }

        if (BasicFunctions.isNotEmpty(pAppointments.personAppointments)
                && pAppointments.personAppointments.isValid()) {
            person = Person.findById(pAppointments.personAppointments.id);
        }

        if (BasicFunctions.isNotEmpty(pAppointments.organizationAppointments)
                && pAppointments.organizationAppointments.isValid()) {
            organization = Organization.findById(pAppointments.organizationAppointments.id);
        }

        try {

            if (BasicFunctions.isEmpty(pAppointments) || BasicFunctions.isEmpty(pAppointments.appointmentDate)
                    && BasicFunctions.isEmpty(pAppointments.professionalAppointments)
                    && BasicFunctions.isEmpty(pAppointments.personAppointments)
                    && BasicFunctions.isEmpty(pAppointments.organizationAppointments)) {
                throw new BadRequestException("Enter data for the Appointments.");
            } else {
                if (BasicFunctions.isNotEmpty(pAppointments.appointmentDate)) {
                    Appointments.appointmentDate = pAppointments.appointmentDate;
                }
                if (BasicFunctions.isNotEmpty(pAppointments.appointmentTime)) {
                    Appointments.appointmentTime = pAppointments.appointmentTime;
                }
                if (BasicFunctions.isNotEmpty(pAppointments.professionalAppointments)) {
                    Appointments.professionalAppointments = pAppointments.professionalAppointments;
                }
                if (BasicFunctions.isNotEmpty(pAppointments.personAppointments)) {
                    Appointments.personAppointments = pAppointments.personAppointments;
                }
                if (BasicFunctions.isNotEmpty(pAppointments.organizationAppointments)) {
                    Appointments.organizationAppointments = pAppointments.organizationAppointments;
                }
                if (BasicFunctions.isNotEmpty(profissional)) {
                    Appointments.professionalAppointments = profissional;
                    Appointments.professionalName = profissional.person.name;
                }
                if (BasicFunctions.isNotEmpty(organization)) {
                    Appointments.organizationAppointments = organization;
                }
                if (BasicFunctions.isNotEmpty(pAppointments.preference)) {
                    Appointments.preference = pAppointments.preference;
                } else {
                    Appointments.preference = Boolean.FALSE;
                }
                Appointments.personName = person.name;
                Appointments.bookingStatus = BookingStatus.findById(pAppointments.bookingStatus.id);
                Appointments.updatedBy = userAuth;
                Appointments.updatedAt = LocalDateTime.now();
                Appointments.persist();

                responses.status = 200;
                responses.data = Appointments;
                responses.messages.add("Appointments register with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = Appointments;
            responses.messages.add("cannot apply the Appointments.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteAgendamento(@NotNull List<Long> pListIdAgendamento) {

        Responses responses;
        List<Appointments> appointments;
        List<Appointments> appointmentsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        appointments = PanacheEntityBase.list("id in ?1 and active = true", pListIdAgendamento);
        int count = appointments.size();

        try {

            if (appointments.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Agendamentos not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            appointments.forEach((appointment) -> {
                appointment.bookingStatus = BookingStatus.findById(appointment.serviceType.id);
                appointment.updatedBy = userAuth;
                appointment.active = Boolean.FALSE;
                appointment.updatedAt = LocalDateTime.now();
                appointment.deletedAt = LocalDateTime.now();
                appointment.persist();
                appointmentsAux.add(appointment);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = this.Appointments;
                responses.messages.add("Appointments successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(appointmentsAux);
                responses.messages.add(count + " Appointments successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = this.Appointments;
                responses.messages.add("Appointments not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(Appointments);
                responses.messages.add("Appointments not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivateAgendamento(@NotNull List<Long> pListIdAgendamento) {

        Responses responses;
        List<Appointments> appointments;
        List<Appointments> appointmentsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        User userAuth = Context.getContextUser(context);
        appointments = PanacheEntityBase.list("id in ?1 and active = false", pListIdAgendamento);
        int count = appointments.size();

        try {

            if (appointments.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Appointments not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            appointments.forEach((appointment) -> {
                appointment.bookingStatus = BookingStatus.findById(appointment.serviceType.id);
                appointment.updatedBy = userAuth;
                appointment.active = Boolean.TRUE;
                appointment.updatedAt = LocalDateTime.now();
                appointment.deletedAt = LocalDateTime.now();
                appointment.persist();
                appointmentsAux.add(appointment);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = this.Appointments;
                responses.messages.add("Appointments reactived with sucessful!");
            } else {
                responses.datas = Collections.singletonList(appointmentsAux);
                responses.messages.add(count + " Appointments reactived with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = this.Appointments;
                responses.messages.add("Appointments not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(Appointments);
                responses.messages.add("Appointments not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Appointments setBookingStatus(Appointments pAppointments) {
        Appointments Appointments = new Appointments();
        if (pAppointments.isValid()) {
            Appointments = PanacheEntityBase.findById(pAppointments.id);
            if (Appointments.isValid()) {
                Appointments.bookingStatus = BookingStatus.findById(BookingStatus.RESCHEDULED);
                Appointments.persist();
            }
        }
        return Appointments;
    }

    public List<Appointments> makeFreeAppointments(List<Appointments> plistaAppointmentsFree,
            List<Appointments> pListaAppointmentsPersisted) {

        return plistaAppointmentsFree.stream()
                .filter(x -> pListaAppointmentsPersisted.stream()
                        .noneMatch(y -> y.professionalAppointments.id.equals(x.professionalAppointments.id)
                                && (y.appointmentDate.equals(x.appointmentDate)
                                        && y.appointmentTime.equals(x.appointmentTime)
                                        && y.bookingStatus.id.equals(BookingStatus.SCHEDULED))))
                .collect(Collectors.toList());
    }
}
