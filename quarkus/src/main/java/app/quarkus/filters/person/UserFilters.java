package app.quarkus.filters.person;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.User;

import static app.core.utils.StringBuilder.makeQueryString;

public class UserFilters {

    public static String makeUserQueryStringByFilters(Long id, String login, Long personId,
                                                      Long organizationDefaultId, String professionalName, String user, Boolean bot) {
        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", User.class);
        }
        if (BasicFunctions.isNotEmpty(login)) {
            queryString += makeQueryString(login, "login", User.class);
        }
        if (BasicFunctions.isValid(personId)) {
            queryString += makeQueryString(personId, "personId", User.class);
        }
        if (BasicFunctions.isValid(organizationDefaultId)) {
            queryString += makeQueryString(organizationDefaultId, "organizationDefaultId", User.class);
        }
        if (BasicFunctions.isNotEmpty(professionalName)) {
            queryString += makeQueryString(professionalName, "professionalName", User.class);
        }
        if (BasicFunctions.isNotEmpty(user)) {
            queryString += makeQueryString(user, "user", User.class);
        }
        if (BasicFunctions.isValid(bot)) {
            queryString += makeQueryString(bot, "bot", User.class);
        }

        return queryString;
    }
}
