package app.quarkus.filters.bookingStatus;

import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.BookingStatus;

import static app.core.utils.StringBuilder.makeQueryString;

public class StatusFilters {

    public static String makeBookingStatusQueryStringByFilters(Long id, String status) {

        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", BookingStatus.class);
        }
        if (BasicFunctions.isNotEmpty(status)) {
            queryString += makeQueryString(status, "status", BookingStatus.class);
        }

        return queryString;
    }
}
