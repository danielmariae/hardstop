package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LoteResponseDTO;
import br.unitins.topicos1.service.LoteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/lotes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoteResource {

    @Inject
    LoteService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @Transactional
    @RolesAllowed({"Func", "Admin"})
    public Response insert (LoteDTO dto){
        LoteResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }


    @PUT
    @Transactional
    @RolesAllowed({"Func", "Admin"})
    @Path("/put/{id}")
    public Response update(
            LoteDTO dto,
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
    @RolesAllowed({"Func", "Admin"})
    @Path("/delete/{id}")
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
    @RolesAllowed({"Func", "Admin"})
    @Path("/search/id/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }
}
