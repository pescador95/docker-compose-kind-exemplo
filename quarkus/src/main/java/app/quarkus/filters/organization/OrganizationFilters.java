package app.quarkus.filters.organization;

import app.core.utils.BasicFunctions;
import app.quarkus.model.organization.Organization;

import static app.core.utils.StringBuilder.makeQueryString;

public class OrganizationFilters {

    public static String makeOrganizationQueryStringByFilters(String name, String cnpj, String telephone,
                                                              String cellphone,
                                                              String email, Long addressId, Long tipoAgendamento_Id) {
        String queryString = "";

        if (BasicFunctions.isNotEmpty(name)) {
            queryString += makeQueryString(name, "name", Organization.class);
        }
        if (BasicFunctions.isNotEmpty(cnpj)) {
            queryString += makeQueryString(cnpj, "cnpj", Organization.class);
        }
        if (BasicFunctions.isNotEmpty(telephone)) {
            queryString += makeQueryString(telephone, "telephone", Organization.class);
        }
        if (BasicFunctions.isNotEmpty(cellphone)) {
            queryString += makeQueryString(cellphone, "cellphone", Organization.class);
        }
        if (BasicFunctions.isNotEmpty(email)) {
            queryString += makeQueryString(email, "email", Organization.class);
        }
        if (BasicFunctions.isValid(addressId)) {
            queryString += makeQueryString(addressId, "addressId", Organization.class);
        }
        if (BasicFunctions.isValid(tipoAgendamento_Id)) {
            queryString += makeQueryString(tipoAgendamento_Id, "tipoAgendamento_Id", Organization.class);
        }
        return queryString;
    }
}
