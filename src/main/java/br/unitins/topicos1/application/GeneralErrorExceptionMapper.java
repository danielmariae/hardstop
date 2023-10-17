package br.unitins.topicos1.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class GeneralErrorExceptionMapper implements ExceptionMapper<GeneralErrorException> {
    @Override
    public Response toResponse(GeneralErrorException exception) {
        GeneralError ge = new GeneralError(exception.getCode(), exception.getCodeMessage());
        ge.setSubjectError(exception.getSubjectName(), exception.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(ge).build();
    }
    
}
