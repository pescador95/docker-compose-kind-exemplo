package app.core.filters.auth;

import app.core.utils.BasicFunctions;

import javax.management.relation.Role;

import static app.core.utils.StringBuilder.makeQueryString;

public class RoleFilters {

    public static String makeRoleQueryStringByFilters(Long id, String name, String role, Boolean admin,
                                                      Long userId) {

        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", Role.class);
        }
        if (BasicFunctions.isNotEmpty(name)) {
            queryString += makeQueryString(name, "name", Role.class);
        }
        if (BasicFunctions.isNotEmpty(role)) {
            queryString += makeQueryString(role, "role", Role.class);
        }
        if (BasicFunctions.isValid(admin)) {
            queryString += makeQueryString(admin, "admin", Role.class);
        }
        if (BasicFunctions.isValid(userId)) {
            queryString += makeQueryString(userId, "userId", Role.class);
        }

        return queryString;
    }
}
