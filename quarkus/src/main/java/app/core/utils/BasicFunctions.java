package app.core.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BasicFunctions {
    public static <T> boolean isNotEmpty(T object) {
        if (object == null) {
            return false;
        }

        if (object instanceof String) {
            return !((String) object).isEmpty();
        }

        if (object instanceof List) {
            return !((List<?>) object).isEmpty();
        }

        if (object instanceof Integer) {
            return (Integer) object != 0;
        }

        if (object instanceof Double) {
            return (Double) object != 0.0;
        }

        if (object instanceof Float) {
            return (Float) object != 0.0f;
        }

        if (object instanceof Long) {
            return (Long) object != 0;
        }

        if (object instanceof Boolean) {
            return (Boolean) object;
        }

        if (object instanceof BigDecimal) {
            return ((BigDecimal) object).compareTo(BigDecimal.ZERO) != 0;
        }

        if (object instanceof Object[]) {
            return ((Object[]) object).length != 0;
        }

        if (object instanceof byte[]) {
            return ((byte[]) object).length != 0;
        }
        return true;
    }

    public static <T> boolean isEmpty(T object) {
        return !isNotEmpty(object);
    }

    public static <t> boolean isValid(t object) {
        if (object == null) {
            return false;
        }

        if (object instanceof String) {
            return !((String) object).isEmpty();
        }

        if (object instanceof List) {
            return !((List<?>) object).isEmpty();
        }

        if (object instanceof Integer) {
            return (Integer) object > 0;
        }

        if (object instanceof Double) {
            return (Double) object > 0.0;
        }

        if (object instanceof Float) {
            return (Float) object > 0.0f;
        }

        if (object instanceof Long) {
            return (Long) object > 0;
        }
        if (object instanceof LocalDate) {
            return ((LocalDate) object).isAfter(LocalDate.MIN) && ((LocalDate) object).isBefore(LocalDate.MAX);
        }
        if (object instanceof LocalTime) {
            return ((LocalTime) object).isAfter(LocalTime.MIN) && ((LocalTime) object).isBefore(LocalTime.MAX);
        }
        return true;
    }

    public static <t> boolean isInvalid(t object) {
        return !isValid(object);
    }
}
