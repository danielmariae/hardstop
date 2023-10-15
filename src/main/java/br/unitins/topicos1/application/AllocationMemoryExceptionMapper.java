package br.unitins.topicos1.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class AllocationMemoryExceptionMapper implements ExceptionMapper<AllocationMemoryException> {
    @Override
    public Response toResponse(AllocationMemoryException exception) {
        AllocationMemoryError allocationmemoryerror = new AllocationMemoryError("500", "Erro de alocação de memória.");
        allocationmemoryerror.setFieldError(exception.getFieldName(), exception.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(allocationmemoryerror).build();
    }
    
}
