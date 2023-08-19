package app.quarkus.controller.appointments;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.quarkus.model.appointments.ServiceType;
import app.quarkus.model.organization.Organization;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class ServiceTypeController {

    private ServiceType serviceType = new ServiceType();

    private Responses responses;

    public Response addTipoAgendamento(@NotNull ServiceType pTipoAgendamento) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        List<Organization> organizations;
        List<Long> organizationsId = new ArrayList<>();

        if (BasicFunctions.isNotEmpty(pTipoAgendamento.organizations)) {
            pTipoAgendamento.organizations.forEach(organization -> organizationsId.add(organization.id));
        }

        organizations = Organization.list("id in ?1", organizationsId);

        if (BasicFunctions.isNotEmpty(pTipoAgendamento.serviceType)) {
            serviceType = ServiceType.find("serviceType = ?1 ", pTipoAgendamento.serviceType)
                    .firstResult();
        }

        if (BasicFunctions.isEmpty(serviceType)) {
            serviceType = new ServiceType();

            if (BasicFunctions.isNotEmpty(pTipoAgendamento.serviceType)) {
                serviceType.serviceType = pTipoAgendamento.serviceType;
            } else {
                responses.messages.add("Input the Service Type for register.");
            }

            if (BasicFunctions.isNotEmpty(organizations)) {
                serviceType.organizations = new ArrayList<>();
                serviceType.organizations.addAll(organizations);
            }
            if (!responses.hasMessages()) {
                serviceType.persist();

                responses.status = 201;
                responses.data = serviceType;
                responses.messages.add("Tipo do Appointments registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = serviceType;
            responses.messages.add("Tipo do Appointments already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateTipoAgendamento(@NotNull ServiceType pServicesType) {

        responses = new Responses();
        responses.messages = new ArrayList<>();
        List<Organization> organizations;
        List<Long> organizationsId = new ArrayList<>();

        if (BasicFunctions.isNotEmpty(pServicesType.organizations)) {
            pServicesType.organizations.forEach(organization -> organizationsId.add(organization.id));
        }

        organizations = Organization.list("id in ?1", organizationsId);

        try {

            if (pServicesType.isValid()) {
                serviceType = PanacheEntityBase.findById(pServicesType.id);
            }
            if (!pServicesType.isValid() && BasicFunctions.isEmpty(pServicesType.serviceType)) {
                throw new BadRequestException("Enter data for update the registration of Service Type.");
            } else {
                if (BasicFunctions.isNotEmpty(pServicesType.serviceType)) {
                    serviceType.serviceType = pServicesType.serviceType;
                }

                if (BasicFunctions.isNotEmpty(organizations)) {
                    serviceType.organizations = new ArrayList<>();
                    serviceType.organizations.addAll(organizations);
                }
                serviceType.persist();
                responses.status = 200;
                responses.data = serviceType;
                responses.messages.add("Service Type updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = serviceType;
            responses.messages.add("cannot update the register of Service Type.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteTipoAgendamento(@NotNull List<Long> pListIdServiceType) {

        List<ServiceType> serviceTypes;
        List<ServiceType> serviceTypesAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        serviceTypes = ServiceType.list("id in ?1", pListIdServiceType);
        int count = serviceTypes.size();

        try {

            if (serviceTypes.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Service Type not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            serviceTypes.forEach((ServiceType) -> {
                ServiceType.delete();
                serviceTypesAux.add(ServiceType);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = serviceType;
                responses.messages.add("Service Type successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(serviceTypesAux);
                responses.messages.add(count + " Service Type successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = serviceType;
                responses.messages.add("Service Type not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(serviceTypes);
                responses.messages.add("Service Type not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
