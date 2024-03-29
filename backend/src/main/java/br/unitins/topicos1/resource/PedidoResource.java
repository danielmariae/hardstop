package br.unitins.topicos1.resource;


import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.service.ClienteService;
import br.unitins.topicos1.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

@Inject
JsonWebToken jwt;

@Inject
ClienteService serviceC;

@Inject
PedidoService service;

  @POST
  @RolesAllowed({"User"})
  @Path("/insert/")
  public Response insert(@Valid PedidoDTO dto, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = serviceC.findByLogin(login).id();

    return Response.status(200).entity(service.insert(dto, id)).build();
  }

  @PATCH
  @RolesAllowed({"Func", "Admin"})
  @Path("/patch/status/")
  public Response updateStatusDoPedido(@Valid PedidoPatchStatusDTO ppsdto) {
    return Response
      .status(200)
      .entity(service.updateStatusDoPedido(ppsdto))
      .build();
  }

  @PATCH
  @Path("/patch/pedido/endereco/")
  @RolesAllowed({"User"})
  public Response updateEndereco(@Valid PedidoPatchEnderecoDTO dto) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = serviceC.findByLogin(login).id();
    return Response.ok(service.updateEndereco(dto, idCliente)).build();
  }

  @GET
  @Path("/search/pedidos/")
  @RolesAllowed({"User"})
  public Response findPedidoByCliente() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = serviceC.findByLogin(login).id();
    return Response.status(200).entity(service.findPedidoByCliente(idCliente)).build();
  }
}
