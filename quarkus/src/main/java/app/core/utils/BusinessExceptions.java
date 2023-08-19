package app.core.utils;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BusinessExceptions implements ExceptionMapper<Exception> {
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Exception error) {

        System.out.println(error);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.getMessage()).build();
    }
}