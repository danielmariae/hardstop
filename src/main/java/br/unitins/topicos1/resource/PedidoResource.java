package br.unitins.topicos1.resource;


import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.service.PedidoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

  @Inject
  PedidoService service;

  
  @PATCH
  @Transactional
  @Path("/patch/status/")
  public Response updateStatusDoPedido(@Valid PedidoPatchStatusDTO ppsdto) {
    return Response
      .status(200)
      .entity(service.updateStatusDoPedido(ppsdto))
      .build();
  }

}
