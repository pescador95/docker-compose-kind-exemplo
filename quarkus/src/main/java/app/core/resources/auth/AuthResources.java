package app.core.resources.auth;

import app.core.controller.auth.AuthController;
import app.core.model.auth.Auth;
import app.core.services.auth.RedisService;
import org.jetbrains.annotations.NotNull;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/auth")
public class AuthResources {

    @Inject
    AuthController authController;

    @Inject
    RedisService redisService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response auth(Auth data) {
        return authController.login(data);
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response refreshToken(Auth data) {
        return authController.refreshToken(data);
    }

    @POST
    @Path("/logout")
    @RolesAllowed("user")
    public Response logout(@Context @NotNull SecurityContext context) {
        return redisService.del();
    }

    @POST
    @Path("/flush")
    @RolesAllowed({"admin"})
    public Response flush() {
        return redisService.flushRedis();
    }
}