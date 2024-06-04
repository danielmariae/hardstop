package br.unitins.topicos1.resource;


import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.dto.endereco.EnderecoDTO;
import br.unitins.topicos1.dto.pedido.PedidoDTO;
import br.unitins.topicos1.dto.pedido.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.pedido.PedidoPatchStatusDTO;
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
import jakarta.ws.rs.PathParam;
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
  public Response insert(@Valid PedidoDTO dto) {
 
    // obtendo o login pelo token jwt
    //String login = jwt.getSubject();

    //Long id = serviceC.findByLogin(login).id();

    return Response.status(200).entity(service.insert(dto)).build();
  }

  @POST
  @RolesAllowed({"User"})
  @Path("/insert/endereco")
  public Response insertEndereco(@Valid EnderecoDTO dto) {
 
    // obtendo o login pelo token jwt
    //String login = jwt.getSubject();

    //Long id = serviceC.findByLogin(login).id();

    return Response.status(200).entity(service.insertEndereco(dto)).build();
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

  @GET
  @Path("/search/pedidos/{id}")
  @RolesAllowed({"User", "Func", "Admin"})
  public Response findPedidoById(@PathParam("id") Long id) {
    return Response.status(200).entity(service.findPedidoById(id)).build();
  }

  @GET
  @Path("/search/pedidos/cartao/{id}")
  @RolesAllowed({"User", "Func", "Admin"})
  public Response findCartaoByPedido(@PathParam("id") Long id){
    return Response.status(200).entity(service.findCartaoByPedido(id)).build();
  }

  @GET
  @Path("/search/pedidos/status/{id}")
  @RolesAllowed({"User", "Func", "Admin"})
  public Response findStatusPedidoById(@PathParam("id") Long id){
    return Response.status(200).entity(service.findByStatus(id)).build();
  }




}
