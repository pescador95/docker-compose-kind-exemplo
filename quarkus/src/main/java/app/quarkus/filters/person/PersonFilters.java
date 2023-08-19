package app.quarkus.filters.person;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.Person;

import java.time.LocalDate;

import static app.core.utils.StringBuilder.makeQueryString;

public class PersonFilters {

    public static String makePersonQueryStringByFilters(Long id, String name, Long genderId, LocalDate birthDay,
                                                        String telephone, String cellphone, String email, Long addressId, String cpf) {

        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", Person.class);
        }
        if (BasicFunctions.isNotEmpty(name)) {
            queryString += makeQueryString(name, "name", Person.class);
        }
        if (BasicFunctions.isValid(genderId)) {
            queryString += makeQueryString(genderId, "genderId", Person.class);
        }
        if (BasicFunctions.isValid(addressId)) {
            queryString += makeQueryString(addressId, "addressId", Person.class);
        }
        if (BasicFunctions.isNotEmpty(email)) {
            queryString += makeQueryString(email, "email", Person.class);
        }
        if (BasicFunctions.isNotEmpty(telephone)) {
            queryString += makeQueryString(telephone, "telephone", Person.class);
        }
        if (BasicFunctions.isNotEmpty(cellphone)) {
            queryString += makeQueryString(cellphone, "cellphone", Person.class);
        }
        if (BasicFunctions.isNotEmpty(birthDay)) {
            queryString += makeQueryString(birthDay, "birthDay", Person.class);
        }
        if (BasicFunctions.isNotEmpty(cpf)) {
            queryString += makeQueryString(cpf, "cpf", Person.class);
        }

        return queryString;
    }
}
