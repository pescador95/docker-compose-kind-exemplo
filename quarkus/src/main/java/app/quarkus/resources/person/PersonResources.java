package app.quarkus.resources.person;

import app.core.model.DTO.Responses;
import app.quarkus.controller.person.PersonController;
import app.quarkus.model.person.Person;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static app.quarkus.filters.person.PersonFilters.makePersonQueryStringByFilters;

@Path("/person")
public class PersonResources {

    @Inject
    PersonController controller;
    Person person;
    Responses responses;
    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user", "bot" })
    public Response getById(@PathParam("id") Long pId) {
        person = Person.findById(pId);
        return Response.ok(person).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user", "bot" })

    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active,
            @Context @NotNull SecurityContext context) {

        query = "active = " + active;
        long count = Person.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response list(
            @QueryParam("id") Long id,
            @QueryParam("name") String name,
            @QueryParam("generoId") Long generoId,
            @QueryParam("dataNascimento") LocalDate dataNascimento,
            @QueryParam("telephone") String telephone,
            @QueryParam("cellphone") String cellphone,
            @QueryParam("email") String email,
            @QueryParam("addressId") Long addressId,
            @QueryParam("cpf") String cpf,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        String queryString = makePersonQueryStringByFilters(id, name, generoId, dataNascimento, telephone, cellphone,
                email, addressId, cpf);
        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Person> person;
        person = Person.find(query);
        List<Person> personsFiltradas = person.page(Page.of(pageIndex, pageSize)).list().stream()
                .filter(c -> (c.active = active))
                .collect(Collectors.toList());

        return Response.ok(personsFiltradas).status(200).build();

    }

    @GET
    @Path("/cpf")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response listByCPF(@QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("cpf") String cpf,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        query = "active = " + active + " and cpf = '" + cpf + "'";

        PanacheQuery<Person> person;
        person = Person.find(query);
        List<Person> personsFiltradas = person.page(Page.of(pageIndex, pageSize)).list().stream()
                .filter(c -> (c.active = active))
                .collect(Collectors.toList());

        return Response.ok(personsFiltradas).status(200).build();
    }

    @GET
    @Path("/phone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response listByPhone(@QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("telephone") String telephone,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        query = "active = " + active + " and telephone = '" + telephone + "' or cellphone = '" + telephone + "'";

        PanacheQuery<Person> person;
        person = Person.find(query);
        List<Person> personsFiltradas = person.page(Page.of(pageIndex, pageSize)).list().stream()
                .filter(c -> (c.active = active))
                .collect(Collectors.toList());

        return Response.ok(personsFiltradas).status(200).build();
    }

    @GET
    @Path("/ident")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response listByIdent(@QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("ident") String ident,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        query = "active = " + active + " and telephone = '" + ident + "' or cellphone = '" + ident + "'" + " or cpf = '"
                + ident + "'";

        PanacheQuery<Person> person;
        person = Person.find(query);
        List<Person> personsFiltradas = person.page(Page.of(pageIndex, pageSize)).list().stream()
                .filter(c -> (c.active = active))
                .collect(Collectors.toList());

        return Response.ok(personsFiltradas).status(200).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response add(Person pPerson, @Context @NotNull SecurityContext context) {
        try {
            return controller.addPerson(pPerson);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Person.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/bot/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response addByBot(Person pPerson, @Context @NotNull SecurityContext context) {
        try {
            return controller.addPerson(pPerson);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Person.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response update(Person pPerson, @Context @NotNull SecurityContext context) {
        try {

            return controller.updatePerson(pPerson);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update a Person.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response deleteList(List<Long> pListPerson, @Context @NotNull SecurityContext context) {
        try {

            return controller.deletePerson(pListPerson);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListPerson.size() <= 1) {
                responses.messages.add("cannot delete the Person.");
            } else {
                responses.messages.add("cannot delete the Persons.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/reactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response reactivateList(List<Long> pListPerson, @Context @NotNull SecurityContext context) {
        try {

            return controller.reactivatePerson(pListPerson);
        } catch (Exception e) {
            if (pListPerson.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar a Person.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reativar as Persons.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
