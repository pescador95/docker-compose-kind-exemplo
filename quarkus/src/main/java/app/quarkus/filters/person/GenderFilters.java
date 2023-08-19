package app.quarkus.filters.person;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.Gender;

import static app.core.utils.StringBuilder.makeQueryString;

public class GenderFilters {

    public static String makeGenderQueryStringByFilters(Long id, String gender) {

        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", Gender.class);
        }
        if (BasicFunctions.isNotEmpty(gender)) {
            queryString += makeQueryString(gender, "gender", Gender.class);
        }

        return queryString;
    }
}
