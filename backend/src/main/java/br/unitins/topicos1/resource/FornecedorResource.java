package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.FornecedorDTO;
import br.unitins.topicos1.dto.FornecedorResponseDTO;
import br.unitins.topicos1.service.FornecedorService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.postgresql.util.PSQLException;

@Path("/fornecedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FornecedorResource {

    @Inject
    FornecedorService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed({"Func", "Admin"})
    public Response insert (@Valid FornecedorDTO dto){
        FornecedorResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }


    @PUT
    @RolesAllowed({"Func", "Admin"})
    @Path("/{id}")
    public Response update(@Valid FornecedorDTO dto, @PathParam("id") Long id)
    {
        service.update(dto, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Func", "Admin"})
    public Response delete(@PathParam("id") Long id){
        try {
            service.delete(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (PSQLException e) {
            // Trate a exceção específica de violação de restrição de chave estrangeira
            Map<String, Object> responseBod = new HashMap<>();
            responseBod.put("message", e.getCause().getCause().getLocalizedMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(responseBod).build();
        } catch (Exception e) {
            // Outras exceções que podem ocorrer durante a exclusão
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getCause().getCause().getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseBody).build();
            // return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ocorreu um erro durante a exclusão do fornecedor.").build();
        }
    }

    @GET
    @RolesAllowed({"Func", "Admin"})
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("pageSize") @DefaultValue("100") int pageSize) {

        return Response.ok(service.findByAll(page, pageSize)).build();
    }

    @GET
    @RolesAllowed({"Func", "Admin"})
    public Response findTodos() {
        return Response.ok(service.findTodos()).build();
    }

    @GET
    @Path("/count")
    public Long count() {
        return service.count();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Func", "Admin"})
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }
}
