package br.unitins.topicos1.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // String message = "Erro ao excluir fornecedor: Violação de restrição de chave estrangeira";
        // return Response.status(Response.Status.BAD_REQUEST)
        //         .entity(message)
        //         .build();
        ValidationError validationError = new ValidationError("400", "Erro de validação");
        validationError.addFieldError(exception.getLocalizedMessage(), exception.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(validationError).build();
    }
}
