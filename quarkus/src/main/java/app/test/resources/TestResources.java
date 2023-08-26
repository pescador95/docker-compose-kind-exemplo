package app.test.resources;

import app.core.model.DTO.Responses;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
public class TestResources {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    @PermitAll
    public Response helloWorld() {
        String hello = "Hello World!";

        Responses responses = new Responses();
        responses.status = 200;
        responses.data = hello;
        responses.messages.add("Hello World!");
        System.out.println(hello);
        return Response.ok(responses).status(200).build();
    }
}
