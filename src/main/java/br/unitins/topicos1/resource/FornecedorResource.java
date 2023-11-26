package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.FornecedorDTO;
import br.unitins.topicos1.dto.FornecedorResponseDTO;
import br.unitins.topicos1.service.FornecedorService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/lotes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FornecedorResource {

    @Inject
    FornecedorService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @Transactional
    public Response insert (FornecedorDTO dto){
        FornecedorResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }


    @PUT
    @Transactional
    @Path("/put/{id}")
    public Response update(
            FornecedorDTO dto,
            @PathParam("id") Long id)
    {
        service.update(dto, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // @PATCH
    // @Transactional
    // @Path("patch/classificacao/{id}")
    // public Response updateClassificacao(
    //     @Valid List<ClassificacaoDTO> cls,
    //     @PathParam("id") Long id)
    // {
    //     return Response.status(200).entity(service.updateClassificacao(cls, id)).build();
    // }


    @DELETE
    @Transactional
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response findAll() {
        return Response.ok(service.findByAll()).build();
    }

    @GET
    @Path("/search/id/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }
}
