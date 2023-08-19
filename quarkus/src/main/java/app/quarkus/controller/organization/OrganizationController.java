package app.quarkus.controller.organization;

import app.core.model.DTO.Responses;
import app.core.utils.BasicFunctions;
import app.core.utils.Context;
import app.quarkus.model.address.Address;
import app.quarkus.model.organization.Organization;
import app.quarkus.model.person.User;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class OrganizationController {

    @javax.ws.rs.core.Context
    SecurityContext context;
    private Organization organization = new Organization();
    private Responses responses;
    private User userAuth;
    private Address address;

    public Response addOrganization(@NotNull Organization pOrganization) {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);
        address = Address.findById(pOrganization.address.id);

        if (BasicFunctions.isNotEmpty(pOrganization.identityDocument)) {
            organization = Organization.find("cnpj = ?1 and active = true", pOrganization.identityDocument)
                    .firstResult();
        }

        if (BasicFunctions.isEmpty(organization)) {
            organization = new Organization();

            if (BasicFunctions.isNotEmpty(pOrganization.name)) {
                organization.name = pOrganization.name;
            }
            if (BasicFunctions.isNotEmpty(pOrganization.identityDocument)) {
                organization.identityDocument = pOrganization.identityDocument;
            }
            if (BasicFunctions.isNotEmpty(pOrganization.telephone)) {
                organization.telephone = pOrganization.telephone;
            }
            if (BasicFunctions.isNotEmpty(pOrganization.cellphone)) {
                organization.cellphone = pOrganization.cellphone;
            }
            if (BasicFunctions.isNotEmpty(address)) {
                organization.address = address;
            }
            if (BasicFunctions.isNotEmpty(pOrganization.email)) {
                organization.email = pOrganization.email;
            }
            if (!responses.hasMessages()) {
                organization.user = userAuth;
                organization.updatedBy = userAuth;
                organization.active = Boolean.TRUE;
                organization.updatedAt = LocalDateTime.now();
                organization.persist();

                responses.status = 201;
                responses.data = organization;
                responses.messages.add("Organization registered successfully!");

            } else {
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } else {
            responses.status = 400;
            responses.data = organization;
            responses.messages.add("Organization already registered!");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response updateOrganization(@NotNull Organization pOrganization) {

        userAuth = Context.getContextUser(context);
        address = Address.findById(pOrganization.address.id);

        responses = new Responses();
        responses.messages = new ArrayList<>();

        try {

            if (pOrganization.isValid()) {
                organization = Organization.findById(pOrganization.id);
            }
            if (!pOrganization.isValid() && BasicFunctions.isEmpty(pOrganization.name)
                    && BasicFunctions.isEmpty(pOrganization.identityDocument)) {
                throw new BadRequestException("Enter data for update o cadastro da Organization.");
            } else {
                if (BasicFunctions.isNotEmpty(pOrganization.name)) {
                    organization.name = pOrganization.name;
                }
                if (BasicFunctions.isNotEmpty(pOrganization.identityDocument)) {
                    organization.identityDocument = pOrganization.identityDocument;
                }
                if (BasicFunctions.isNotEmpty(pOrganization.telephone)) {
                    organization.telephone = pOrganization.telephone;
                }
                if (BasicFunctions.isNotEmpty(pOrganization.cellphone)) {
                    organization.cellphone = pOrganization.cellphone;
                }
                if (BasicFunctions.isNotEmpty(address)) {
                    organization.address = address;
                }
                if (BasicFunctions.isNotEmpty(pOrganization.email)) {
                    organization.email = pOrganization.email;
                }
                organization.updatedBy = userAuth;
                organization.updatedAt = LocalDateTime.now();
                organization.persist();

                responses.status = 200;
                responses.data = organization;
                responses.messages.add("Cadastro de Organization updated successfully!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            responses.data = organization;
            responses.messages.add("cannot update o cadastro da Organization.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response deleteOrganization(@NotNull List<Long> pListIdOrganization) {

        List<Organization> organizations;
        List<Organization> organizationsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        userAuth = Context.getContextUser(context);

        organizations = Organization.list("id in ?1 and active = true", pListIdOrganization);
        int count = organizations.size();

        try {

            if (organizations.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Organizations not found or already deleted.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            organizations.forEach((organization) -> {

                organization.updatedBy = userAuth;
                organization.active = Boolean.FALSE;
                organization.updatedAt = LocalDateTime.now();
                organization.deletedAt = LocalDateTime.now();
                organization.persist();
                organizationsAux.add(organization);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = organization;
                responses.messages.add("Organization successfully deleted!");
            } else {
                responses.datas = Collections.singletonList(organizationsAux);
                responses.messages.add(count + " Organizations successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = organization;
                responses.messages.add("Organization not found or already deleted.");
            } else {
                responses.datas = Collections.singletonList(organizations);
                responses.messages.add("Organizations not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    public Response reactivateOrganization(@NotNull List<Long> pListIdOrganization) {

        List<Organization> organizations;
        List<Organization> organizationsAux = new ArrayList<>();
        responses = new Responses();
        responses.messages = new ArrayList<>();

        User userAuth = Context.getContextUser(context);
        organizations = Organization.list("id in ?1 and active = false", pListIdOrganization);
        int count = organizations.size();

        try {

            if (organizations.isEmpty()) {
                responses.status = 400;
                responses.messages.add("Organizations not located or already reactivated.");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }

            organizations.forEach((organization) -> {

                organization.updatedBy = userAuth;
                organization.active = Boolean.TRUE;
                organization.updatedAt = LocalDateTime.now();
                organization.deletedAt = LocalDateTime.now();
                organization.persist();
                organizationsAux.add(organization);
            });
            responses.status = 200;
            if (count <= 1) {
                responses.data = organization;
                responses.messages.add("Organization reactivated with sucessful!");
            } else {
                responses.datas = Collections.singletonList(organizationsAux);
                responses.messages.add(count + " Organizations reactivated with sucessful!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.data = organization;
                responses.messages.add("Organization not located or already reactivated.");
            } else {
                responses.datas = Collections.singletonList(organizations);
                responses.messages.add("Organizations not located or already reactivated.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
