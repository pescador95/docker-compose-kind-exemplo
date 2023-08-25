package app.core.resources.auth;

import app.core.controller.auth.RecoveryPasswordController;
import app.core.model.DTO.Responses;
import app.quarkus.model.person.User;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/recoverPassword")

public class RecoveryPasswordResource {
    @Inject
    RecoveryPasswordController controller;

    Responses responses;

    @POST
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response sendMail(@PathParam("email") String email) {
        try {
            return controller.sendEmail(email);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("Unable to locate a record with the email provided.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @RolesAllowed({ "user" })
    public Response update(User pUser, @Context @NotNull SecurityContext context,
            @QueryParam("password") String password) {
        try {

            return controller.updatePassword(password);
        } catch (Exception e) {
            responses = new Responses();
            responses.status = 400;
            responses.messages.add("cannot update a senha.");
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}