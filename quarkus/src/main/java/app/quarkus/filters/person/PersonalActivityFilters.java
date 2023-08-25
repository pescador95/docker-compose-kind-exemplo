package app.quarkus.filters.person;

import app.core.utils.BasicFunctions;
import app.quarkus.model.person.PersonalActivity;

import static app.core.utils.StringBuilder.makeQueryString;

public class PersonalActivityFilters {

    public static String makePersonalActivityQueryStringByFilters(Long id, String queixaPrincipal, String medicamentos,
            String diagnosticoClinico, String comorbidades, String ocupacao, String responsibleContato,
            String personName) {
        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(queixaPrincipal)) {
            queryString += makeQueryString(queixaPrincipal, "queixaPrincipal", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(medicamentos)) {
            queryString += makeQueryString(medicamentos, "medicamentos", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(diagnosticoClinico)) {
            queryString += makeQueryString(diagnosticoClinico, "diagnosticoClinico", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(comorbidades)) {
            queryString += makeQueryString(comorbidades, "comorbidades", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(ocupacao)) {
            queryString += makeQueryString(ocupacao, "ocupacao", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(responsibleContato)) {
            queryString += makeQueryString(responsibleContato, "responsibleContato", PersonalActivity.class);
        }
        if (BasicFunctions.isNotEmpty(personName)) {
            queryString += makeQueryString(personName, "personName", PersonalActivity.class);
        }
        return queryString;
    }
}
