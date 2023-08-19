package app.quarkus.filters.address;

import app.core.utils.BasicFunctions;
import app.quarkus.model.address.Address;

import static app.core.utils.StringBuilder.makeQueryString;

public class AddressFilters {

    public static String makeAdressQueryStringByFilters(String zipCode, String publicPlace, Long number,
                                                        String complement, String city, String state) {
        String queryString = "";

        if (BasicFunctions.isNotEmpty(zipCode)) {
            queryString += makeQueryString(zipCode, "zipCode", Address.class);
        }
        if (BasicFunctions.isNotEmpty(publicPlace)) {
            queryString += makeQueryString(publicPlace, "publicPlace", Address.class);
        }
        if (BasicFunctions.isValid(number)) {
            queryString += makeQueryString(number, "number", Address.class);
        }
        if (BasicFunctions.isNotEmpty(complement)) {
            queryString += makeQueryString(complement, "complement", Address.class);
        }
        if (BasicFunctions.isNotEmpty(city)) {
            queryString += makeQueryString(city, "city", Address.class);
        }
        if (BasicFunctions.isNotEmpty(state)) {
            queryString += makeQueryString(state, "state", Address.class);
        }
        return queryString;
    }
}
