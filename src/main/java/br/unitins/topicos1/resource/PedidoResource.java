package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.service.PedidoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

  @Inject
  PedidoService service;

  @POST
  @Transactional
  @Path("/insert/{id}")
  public Response insert(@Valid PedidoDTO dto, @PathParam("id") Long id) {
    return Response.ok(service.insert(dto, id)).build();
  }

  @DELETE
  @Transactional
  @Path("/delete/{id}")
  public Response deletePedidoByCliente(@PathParam("id") Long id, Long idPedido) {
    return Response.ok().build();
  }

  @PATCH
  @Transactional
  @Path("/patch/status/")
  public Response updateStatusDoPedido(@Valid PedidoPatchStatusDTO ppsdto) {
    return Response
      .status(200)
      .entity(service.updateStatusDoPedido(ppsdto))
      .build();
  }

  @PATCH
  @Transactional
  @Path("patch/endereco/{id}")
  public Response updateEndereco(PedidoPatchEnderecoDTO dto, @PathParam("id") Long id) {
    return Response.ok(service.updateEndereco(dto, id)).build();
  }

  @GET
  @Path("/search/id/{id}")
  public Response findPedidoByCliente(@PathParam("id") Long id) {
    return Response.ok(service.findPedidoByCliente(id)).build();
  }
}
