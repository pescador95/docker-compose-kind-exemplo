package app.core.filters.profile;

import app.core.model.profile.Routine;
import app.core.utils.BasicFunctions;

import static app.core.utils.StringBuilder.makeQueryString;

public class RoutineFilters {

    public static String makeRoutineQueryStringByFilters(Long id, String name, String icon, String path,
                                                         String title) {
        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", Routine.class);
        }
        if (BasicFunctions.isNotEmpty(name)) {
            queryString += makeQueryString(name, "name", Routine.class);
        }
        if (BasicFunctions.isNotEmpty(icon)) {
            queryString += makeQueryString(icon, "icon", Routine.class);
        }
        if (BasicFunctions.isNotEmpty(path)) {
            queryString += makeQueryString(path, "path", Routine.class);
        }
        if (BasicFunctions.isNotEmpty(title)) {
            queryString += makeQueryString(title, "title", Routine.class);
        }
        return queryString;
    }
}
