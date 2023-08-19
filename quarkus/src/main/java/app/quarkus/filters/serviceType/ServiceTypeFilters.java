package app.quarkus.filters.serviceType;

import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.ServiceType;
import io.quarkus.panache.common.Parameters;

import java.util.List;

import static app.core.utils.StringBuilder.makeQueryString;
import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;

public class ServiceTypeFilters {

    public static String makeServiceTypeQueryStringByFilters(Long id, String serviceType) {

        String queryString = "";

        if (BasicFunctions.isValid(id)) {
            queryString += makeQueryString(id, "id", ServiceType.class);
        }
        if (BasicFunctions.isNotEmpty(serviceType)) {
            queryString += makeQueryString(serviceType, "serviceType", ServiceType.class);
        }
        return queryString;
    }

    public static List<ServiceType> findByOrganizationIdAndQueryString(List<Long> organizationId,
                                                                       String queryString) {
        String fullQuery = "organizationId in :orgId";
        fullQuery += " " + queryString;
        return find(fullQuery, Parameters.with("orgId", organizationId)).list();
    }
}
