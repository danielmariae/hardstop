package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LotePatchQDTO;
import br.unitins.topicos1.dto.LotePatchVDTO;
import br.unitins.topicos1.dto.LoteResponseCDTO;
import br.unitins.topicos1.dto.LoteResponseDTO;
import br.unitins.topicos1.service.LoteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/lotes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoteResource {

  @Inject
  LoteService service;

  @Inject
  JsonWebToken jwt;

  @POST
  @RolesAllowed({ "Func", "Admin" })
  @Path("/insert/lote")
  public Response insert(@Valid LoteDTO dto) {
    LoteResponseDTO retorno = service.insert(dto);
    return Response.status(201).entity(retorno).build();
  }

  @POST
  @RolesAllowed({ "Func", "Admin" })
  @Path("/insert2/lote")
  public Response insertTeste(@Valid LoteDTO dto) {
    LoteResponseCDTO retorno = service.insertTeste(dto);
    return Response.status(201).entity(retorno).build();
  }



  @GET
  @RolesAllowed({"Func", "Admin"})
  @Path("/patch/ativalote/{idLote}")
  public Response ativaLote(@PathParam("idLote") Long idLote) {
    return Response.status(201).entity(service.ativaLote(idLote)).build();
  }

  @PATCH
  @RolesAllowed({ "Func", "Admin" })
  @Path("/patch/quantidade")
  public Response updateQuantidade(@Valid LotePatchQDTO dto) {
    return Response.status(201).entity(service.updateQuantidade(dto)).build();
  }

  @PATCH
  @RolesAllowed({ "Func", "Admin" })
  @Path("/patch/valorVenda")
  public Response updateValorVenda(@Valid LotePatchVDTO dto) {
    return Response.ok().entity(service.updateValorVenda(dto)).build();
  }

  @DELETE
  @RolesAllowed({ "Func", "Admin" })
  @Path("/delete/{id}")
  public Response delete(@PathParam("id") Long id) {
    service.delete(id);
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @GET
  @RolesAllowed({ "Func", "Admin" })
  @Path("/search/all/")
  public Response findAll() {
    return Response.ok(service.findByAll()).build();
  }

  @GET
  @RolesAllowed({ "Func", "Admin" })
  @Path("/search/id/{id}")
  public Response findById(@PathParam("id") Long id) {
    return Response.ok(service.findById(id)).build();
  }

  @GET
  @RolesAllowed({ "Func", "Admin" })
  @Path("/search/id2/{id}")
  public Response findByIdTeste(@PathParam("id") Long id) {
    return Response.ok(service.findById(id)).build();
  }


  @GET
  @RolesAllowed({ "Func", "Admin" })
  @Path("/search/idProduto/{id}")
  public Response findByIdProduto(
    @PathParam("id") Long id,
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("pageSize") @DefaultValue("100") int pageSize
  ) {
    return Response.ok(service.findByIdProduto(id, page, pageSize)).build();
  }

  @GET
  @RolesAllowed({ "Func", "Admin" })
  @Path("/search/idProduto2/{id}")
  public Response findByIdProdutoTeste(
    @PathParam("id") Long id,
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("pageSize") @DefaultValue("100") int pageSize
  ) {
    return Response.ok(service.findByIdProdutoTeste(id, page, pageSize)).build();
  }


  @GET
  @Path("/count/{id}")
  public Long count2(@PathParam("id") Long id) {
    return service.count2(id);
  }
  
  @GET
  @Path("/count")
  public Long count() {
    return service.count();
  }

}
