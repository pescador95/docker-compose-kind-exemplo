package app.core.filters.profile;

import app.core.model.profile.ProfileAccess;
import app.core.utils.BasicFunctions;

import static app.core.utils.StringBuilder.makeQueryString;

public class ProfileAccessFilters {

    public static String makeProfileAccessQueryStringByFilters(Long id, String name, Boolean create, Boolean read,
                                                               Boolean update, Boolean delete, Long userId) {
        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", ProfileAccess.class);
        }
        if (BasicFunctions.isNotEmpty(name)) {
            queryString += makeQueryString(name, "name", ProfileAccess.class);
        }
        if (BasicFunctions.isValid(create)) {
            queryString += makeQueryString(create, "create", ProfileAccess.class);
        }
        if (BasicFunctions.isValid(read)) {
            queryString += makeQueryString(read, "read", ProfileAccess.class);
        }
        if (BasicFunctions.isValid(update)) {
            queryString += makeQueryString(update, "update", ProfileAccess.class);
        }
        if (BasicFunctions.isValid(delete)) {
            queryString += makeQueryString(delete, "delete", ProfileAccess.class);
        }
        if (BasicFunctions.isValid(userId)) {
            queryString += makeQueryString(userId, "userId", ProfileAccess.class);
        }
        return queryString;
    }
}
