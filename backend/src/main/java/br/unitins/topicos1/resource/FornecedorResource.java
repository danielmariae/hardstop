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
import org.eclipse.microprofile.jwt.JsonWebToken;

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
    @Path("/delete/{id}")
    @RolesAllowed({"Func", "Admin"})
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @RolesAllowed({"Func", "Admin"})

    public Response findAll() {
        return Response.ok(service.findByAll()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Func", "Admin"})
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }
}
