package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LotePatchDTO;
import br.unitins.topicos1.dto.LoteResponseDTO;
import br.unitins.topicos1.service.LoteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
    @RolesAllowed({"Func", "Admin"})
    @Path("/insert/lote/")
    public Response insert (@Valid LoteDTO dto){
        LoteResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }

    @PATCH
    @RolesAllowed({"Func", "Admin"})
    @Path("/patch/ativalote/{idProduto}")
    public Response ativaLote(@PathParam("idProduto") Long idProduto)
    {
        return Response.status(201).entity(service.ativaLote(idProduto)).build();
    }

    @PATCH
    @RolesAllowed({"Func", "Admin"})
    @Path("/patch/quantidade")
    public Response updateQuantidade(@Valid LotePatchDTO dto)
    {
        return Response.status(201).entity(service.updateQuantidade(dto)).build();
    }

    @DELETE
    @RolesAllowed({"Func", "Admin"})
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @RolesAllowed({"Func", "Admin"})
    @Path("/search/all/")
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
