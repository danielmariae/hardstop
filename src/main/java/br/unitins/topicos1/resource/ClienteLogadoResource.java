package br.unitins.topicos1.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.application.ErrorTP1;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.model.form.ArchiveForm;
import br.unitins.topicos1.service.ClienteService;
import br.unitins.topicos1.service.FileService;
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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

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

@Inject
FileService fileService;

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
  @RolesAllowed({"User"})
  @Path("/insert/pedidos/")
  public Response insert(PedidoDTO dto, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(servicePedido.insert(dto, id)).build();
  }

  @POST
  @RolesAllowed({"User"})
  @Path("/insert/desejos/")
  public Response insertDesejos(Long idProduto) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(servicePedido.insertDesejos(idProduto, id)).build();
  }

  @PATCH
  @Path("patch/telefone/")
  @RolesAllowed({"User"})
  public Response updateTelefoneCliente(@Valid List<TelefonePatchDTO> tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateTelefoneCliente(tel, idCliente)).build();
  }

  @PATCH
  @Path("patch/endereco/")
  @RolesAllowed({"User"})
  public Response updateEnderecoCliente(@Valid List<EnderecoPatchDTO> end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateEnderecoCliente(end, idCliente)).build();
  }

  @DELETE
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
  @Path("/delete/pedido/")
  @RolesAllowed({"User"})
  public Response deletePedidoByCliente(Long idPedido) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    servicePedido.deletePedidoByCliente(idCliente, idPedido);
    return Response.ok().build();
  }

  @DELETE
  @RolesAllowed({"User"})
  @Path("/delete/desejos/")
  public Response deleteDesejos(Long idProduto) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    servicePedido.deleteDesejos(idProduto, id);

    return Response.ok().build();
  }

  @PATCH
  @Path("/patch/pedido/endereco/")
  @RolesAllowed({"User"})
  public Response updateEndereco(PedidoPatchEnderecoDTO dto) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.ok(servicePedido.updateEndereco(dto, idCliente)).build();
  }

  @GET
  @Path("/search/pedidos/")
  @RolesAllowed({"User"})
  public Response findPedidoByCliente() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(servicePedido.findPedidoByCliente(idCliente)).build();
  }

  @PATCH
    @Path("/upload/imagem")
    @RolesAllowed({"User"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@MultipartForm ArchiveForm form) {
        String nomeImagem;
        try {
           nomeImagem = fileService.salvarU(form.getNomeArquivo(), form.getArquivo()); 
        } catch (Exception e) {
            ErrorTP1 error = new ErrorTP1("409", e.getMessage());
            return Response.status(Status.CONFLICT).entity(error).build();
        }

        String login = jwt.getSubject();
        Long idCliente = service.findByLogin(login).id();

        return Response.ok(fileService.updateNomeImagemC(idCliente, nomeImagem)).build();

    }

    @GET
    @Path("/download/imagem/{nomeImagem}")
    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem){
        ResponseBuilder response = Response.ok(fileService.obterU(nomeImagem));
        response.header("Content-Diposition", "attachment; filename="+nomeImagem);
        return response.build();

    }













}
