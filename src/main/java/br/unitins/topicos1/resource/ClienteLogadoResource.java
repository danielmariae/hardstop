package br.unitins.topicos1.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.service.ClienteService;
import br.unitins.topicos1.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientelogado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteLogadoResource {

@Inject
JsonWebToken jwt;

@Inject
ClienteService service;

@Inject
PedidoService servicePedido;

@GET
@RolesAllowed({"User"})
public Response getCliente() {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();
    
    return Response.ok(service.findByLogin(login)).build();
}

  @PATCH
  @Transactional
  @RolesAllowed({"User"})
  @Path("patch/senha/")
  public Response updateSenha(@Valid PatchSenhaDTO senha, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateSenhaCliente(senha, id)).build();
  }

  @POST
  @Transactional
  @RolesAllowed({"User"})
  @Path("pedido/insert/")
  public Response insert(PedidoDTO dto, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(servicePedido.insert(dto, id)).build();
  }

  @PATCH
  @Transactional
  @Path("patch/telefone/")
  @RolesAllowed({"User"})
  public Response updateTelefoneCliente(@Valid List<TelefonePatchDTO> tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateTelefoneCliente(tel, idCliente)).build();
  }

  @PATCH
  @Transactional
  @Path("patch/endereco/")
  @RolesAllowed({"User"})
  public Response updateEnderecoCliente(@Valid List<EnderecoPatchDTO> end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateEnderecoCliente(end, idCliente)).build();
  }

  @DELETE
  @Transactional
  @Path("/delete/")
  @RolesAllowed({"User"})
  public Response deleteCliente() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    service.deleteCliente(idCliente);
    return Response.ok().build();
  }

  @GET
  @Path("/search/listaDesejos/")
  @RolesAllowed({"User"})
  public Response findListaDesejosCliente() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.ok(service.findListaDesejosCliente(idCliente)).build();
  }

  @DELETE
  @Transactional
  @Path("/pedido/delete/")
  @RolesAllowed({"User"})
  public Response deletePedidoByCliente(Long idPedido) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    servicePedido.deletePedidoByCliente(idCliente, idPedido);
    return Response.ok().build();
  }

  @PATCH
  @Transactional
  @Path("/pedido/patch/endereco/")
  @RolesAllowed({"User"})
  public Response updateEndereco(PedidoPatchEnderecoDTO dto) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.ok(servicePedido.updateEndereco(dto, idCliente)).build();
  }

  @GET
  @Path("/pedido/search/")
  @RolesAllowed({"User"})
  public Response findPedidoByCliente() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(servicePedido.findPedidoByCliente(idCliente)).build();
  }

}
