package app.quarkus.filters.appointments;

import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.Appointments;

import java.time.LocalDate;
import java.time.LocalTime;

import static app.core.utils.StringBuilder.makeQueryString;

public class AppointmentsFilters {

    public static String makeAppointmentsQueryStringByFilters(LocalDate appointmentDate, LocalDate startDate,
                                                              LocalDate endDate, LocalTime appointmentTime, LocalTime startTime, LocalTime endTime,
                                                              Long personId, String personName, String professionalName, Long idStatus, Long organizationId,
                                                              Long serviceType, Long professionalId) {

        String queryString = "";

        if (BasicFunctions.isValid(appointmentDate)) {
            queryString += makeQueryString(appointmentDate, "appointmentDate", Appointments.class);
        }
        if (BasicFunctions.isValid(startDate)) {
            queryString += makeQueryString(startDate, "startDate", Appointments.class);
        }
        if (BasicFunctions.isValid(endDate)) {
            queryString += makeQueryString(endDate, "endDate", Appointments.class);
        }
        if (BasicFunctions.isValid(appointmentTime)) {
            queryString += makeQueryString(appointmentTime, "appointmentTime", Appointments.class);
        }
        if (BasicFunctions.isValid(startTime)) {
            queryString += makeQueryString(startTime, "startTime", Appointments.class);
        }
        if (BasicFunctions.isValid(endTime)) {
            queryString += makeQueryString(endTime, "endTime", Appointments.class);
        }
        if (BasicFunctions.isValid(personId)) {
            queryString += makeQueryString(personId, "personId", Appointments.class);
        }
        if (BasicFunctions.isValid(idStatus)) {
            queryString += makeQueryString(idStatus, "bookingStatusId", Appointments.class);
        }
        if (BasicFunctions.isValid(organizationId)) {
            queryString += makeQueryString(organizationId, "organizationId", Appointments.class);
        }
        if (BasicFunctions.isValid(serviceType)) {
            queryString += makeQueryString(serviceType, "serviceType", Appointments.class);
        }
        if (BasicFunctions.isValid(appointmentTime)) {
            queryString += makeQueryString(appointmentTime, "appointmentTime", Appointments.class);
        }
        if (BasicFunctions.isValid(professionalId)) {
            queryString += makeQueryString(professionalId, "professionalId", Appointments.class);
        }
        if (BasicFunctions.isNotEmpty(personName)) {
            queryString += makeQueryString(personName, "personName", Appointments.class);
        }
        if (BasicFunctions.isNotEmpty(professionalName)) {
            queryString += makeQueryString(professionalName, "professionalName", Appointments.class);
        }

        return queryString;
    }
}
