package br.unitins.topicos1.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class RepositoryExceptionMapper implements ExceptionMapper<RepositoryException> {
    @Override
    public Response toResponse(RepositoryException exception) {
        RepositoryError repositoryerror = new RepositoryError("500", "Erro de persistência no banco de dados.");
        repositoryerror.setFieldError(exception.getFieldName(), exception.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(repositoryerror).build();
    }
    
}
