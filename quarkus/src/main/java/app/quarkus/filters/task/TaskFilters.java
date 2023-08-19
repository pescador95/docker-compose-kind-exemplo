package app.quarkus.filters.task;

import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.Task;

import java.time.LocalDate;

import static app.core.utils.StringBuilder.makeQueryString;

public class TaskFilters {

    public static String makeTaskQueryStringByFilters(LocalDate taskDate, LocalDate startDate,
                                                      LocalDate endDate, String userId, String task) {

        String queryString = "";

        if (BasicFunctions.isValid(taskDate)) {
            queryString += makeQueryString(taskDate, "taskDate", Task.class);
        }
        if (BasicFunctions.isValid(startDate)) {
            queryString += makeQueryString(startDate, "startDate", Task.class);
        }
        if (BasicFunctions.isValid(endDate)) {
            queryString += makeQueryString(endDate, "endDate", Task.class);
        }
        if (BasicFunctions.isValid(userId)) {
            queryString += makeQueryString(userId, "userId", Task.class);
        }
        if (BasicFunctions.isNotEmpty(task)) {
            queryString += makeQueryString(task, "task", Task.class);
        }
        return queryString;
    }
}
