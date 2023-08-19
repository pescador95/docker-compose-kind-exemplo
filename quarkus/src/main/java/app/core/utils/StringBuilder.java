package app.core.utils;

import app.quarkus.model.address.Address;
import app.quarkus.model.appointments.Appointments;
import app.quarkus.model.appointments.Task;
import app.quarkus.model.person.Person;

import javax.swing.text.MaskFormatter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class StringBuilder {

    public static String makeMaskCnpjFormatter(String pCnpj) throws ParseException {

        MaskFormatter mask;
        mask = new MaskFormatter("###.###.###/####-##");
        mask.setValueContainsLiteralCharacters(false);
        return mask.valueToString(pCnpj);

    }

    public static String removeChars(String pText) {

        String text = pText.replaceAll("^\\[", "");
        text.replaceAll("\\]", "");
        return text;
    }

    public static List<String> removeCharsList(List<String> pText) throws ParseException {
        List<String> returnTexts = new ArrayList<>();

        pText.forEach(text -> {
            String textReturn;
            textReturn = text.replaceAll("^\\[", "");
            textReturn = text.replaceAll("\\]", "");
            returnTexts.add(textReturn);
        });
        return returnTexts;
    }

    public static String makeOnlyNumbers(String text) throws ParseException {
        return text.replaceAll("[^0-9]+", "");
    }

    public static String makeAddressString(Address pAddress) {
        String address = "";
        address = pAddress.publicPlace + ", " + pAddress.number + ". " + pAddress.city + " - " + pAddress.state;
        return address;
    }

    public static <T> String makeQueryString(T parameterValue, String parameterName, Class<?> entity) {

        String queryString = "";

        if (parameterValue instanceof String) {
            return queryString += " AND LOWER(" + parameterName + ") LIKE '%" + parameterValue.toString().toLowerCase()
                    + "%'";
        }
        if (parameterValue instanceof Integer || parameterValue instanceof Long || parameterValue instanceof Boolean
                || parameterValue instanceof Double || parameterValue instanceof Float
                || parameterValue instanceof BigDecimal || parameterValue instanceof LocalDateTime) {
            return queryString += " AND " + parameterName + " = " + parameterValue;
        }
        if (parameterValue instanceof LocalDate) {
            return queryString += makeEntityAtributeLocalDate(parameterValue, parameterName, entity);
        }
        if (parameterValue instanceof LocalTime) {
            return queryString += makeEntityAtributeLocalTime(parameterValue, parameterName, entity);
        }
        if (parameterValue instanceof List<?> listValues) {
            StringJoiner joiner = new StringJoiner(",", "", "");
            for (Object listValue : listValues) {
                String string = listValue.toString();
                joiner.add(string);
            }
            String paramList = joiner.toString();
            return queryString + " AND " + parameterName + " IN (" + paramList + ")";
        }
        return queryString;
    }

    public static String makeEntityAtributeLocalDate(Object parameterValue, String parameterName, Class<?> entity) {

        if (entity.isInstance(Appointments.class)) {
            if (parameterName.equals("startTime")) {
                return " AND appointmentDate >= " + parameterValue;
            }
            if (parameterName.equals("endDate")) {
                return " AND appointmentDate <= " + parameterValue;
            }
            return " AND appointmentDate = " + parameterValue;
        }
        if (entity.isInstance(Task.class)) {
            if (parameterName.equals("startTime")) {
                return "AND taskDate >= " + parameterValue;
            }
            if (parameterName.equals("endDate")) {
                return " AND taskDate <= " + parameterValue;
            }
            return " AND taskDate = " + parameterValue;
        }
        if (entity.isInstance(Person.class)) {
            if (parameterName.equals("dataNascimento")) {
                return " AND dataNascimento = " + parameterValue;
            }
        }
        return "";
    }

    public static String makeEntityAtributeLocalTime(Object parameterValue, String parameterName, Class<?> entity) {

        if (entity.isInstance(Appointments.class)) {
            if (parameterName.equals("startTime")) {
                return " AND appointmentTime >= " + parameterValue;
            }
            if (parameterName.equals("endTime")) {
                return " AND appointmentTime <= " + parameterValue;
            }
            return " AND appointmentTime = " + parameterValue;
        }
        return "";
    }

    public String makeMaskCpfFormatter(String pCpf) throws ParseException {

        MaskFormatter mask;
        mask = new MaskFormatter("###.###.###-##");
        mask.setValueContainsLiteralCharacters(false);
        return mask.valueToString(pCpf);
    }

    public String makeMaskRgFormatter(String pRg) throws ParseException {

        MaskFormatter mask;
        mask = new MaskFormatter("##.###.###-#");
        mask.setValueContainsLiteralCharacters(false);
        return mask.valueToString(pRg);
    }
}