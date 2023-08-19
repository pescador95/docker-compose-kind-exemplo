package app.quarkus.resources.address;

import app.core.model.DTO.Responses;
import app.quarkus.model.address.Address;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

import static app.quarkus.filters.address.AddressFilters.makeAdressQueryStringByFilters;

@Path("/address")
public class AddressResources {

    @Inject
    app.quarkus.controller.address.AddressController controller;
    Address address;
    Responses responses;
    private String query;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response getById(@PathParam("id") Long pId) {
        address = Address.findById(pId);
        return Response.ok(address).status(200).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response count(@QueryParam("active") @DefaultValue("true") Boolean active,
                          @Context @NotNull SecurityContext context) {

        query = "active = " + active;
        long count = Address.count(query);
        return Response.ok(count).status(200).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response list(
            @QueryParam("zipCode") String zipCode,
            @QueryParam("publicPlace") String publicPlace,
            @QueryParam("number") Long number,
            @QueryParam("complement") String complement,
            @QueryParam("city") String city,
            @QueryParam("state") String state,
            @QueryParam("sort") @DefaultValue("desc") @NotNull String sortQuery,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("active") @DefaultValue("true") Boolean active,
            @QueryParam("strgOrder") @DefaultValue("id") String strgOrder, @Context @NotNull SecurityContext context) {
        String queryString = makeAdressQueryStringByFilters(zipCode, publicPlace, number, complement, city, state);
        query = "active = " + active + " " + queryString + " order by " + strgOrder + " " + sortQuery;
        PanacheQuery<Address> address;
        address = Address.find(query);
        return Response.ok(address.page(Page.of(pageIndex, pageSize)).list().stream().filter(c -> (c.active = active))
                .collect(Collectors.toList())).status(Response.Status.ACCEPTED).build();

    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response add(Address pAddress, @Context @NotNull SecurityContext context) {
        try {
            return controller.addAdress(pAddress);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot register the Address.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response update(Address pAddress, @Context @NotNull SecurityContext context) {
        try {
            return controller.updateAdress(pAddress);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update the Address.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response deleteList(List<Long> pListAdress, @Context @NotNull SecurityContext context) {
        try {
            return controller.deleteAdress(pListAdress);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            if (pListAdress.size() <= 1) {
                responses.messages.add("cannot delete the Address.");
            } else {
                responses.messages.add("cannot delete the Address.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/reactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({"user"})
    public Response reactivateList(List<Long> pListAdress, @Context @NotNull SecurityContext context) {
        try {

            return controller.reactivateAdress(pListAdress);
        } catch (Exception e) {
            if (pListAdress.size() <= 1) {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reactivate the Address.");
            } else {
                responses = new Responses();
                responses.status = 400;
                responses.messages.add("cannot reactivate the Address.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}
