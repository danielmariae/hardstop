package br.unitins.topicos1.resource;


import br.unitins.topicos1.application.ErrorTP1;
import br.unitins.topicos1.dto.*;
import br.unitins.topicos1.model.form.ArchiveForm;
import br.unitins.topicos1.service.UsuarioService;
import br.unitins.topicos1.service.FileService;
import br.unitins.topicos1.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
  @Inject
  UsuarioService service;

  @Inject
  JsonWebToken jwt;

  @Inject
  PedidoService servicePedido;

  @Inject
  FileService fileService;


  @POST
  @Transactional
  public Response insertUsuario(@Valid UsuarioDTO dto) {
    return Response.status(201).entity(service.insertUsuario(dto)).build();
  }

  @GET
  @RolesAllowed({"User"})
  public Response getUsuario() {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    return Response.ok(service.findByLogin(login)).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/nome/")
  public Response updateNome(@Valid PatchNomeDTO nome) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateNome(nome, id)).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/cpf/")
  public Response updateCpf(@Valid PatchCpfDTO cpf) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateCpf(cpf, id)).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/login/")
  public Response updateLogin(@Valid PatchLoginDTO novoLogin) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateLogin(novoLogin, id)).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/email/")
  public Response updateEmail(@Valid PatchEmailDTO email) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateEmail(email, id)).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/senha/")
  public Response updateSenha(@Valid PatchSenhaDTO senha) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateSenhaUsuario(senha, id)).build();
  }

  @POST
  @RolesAllowed({"User"})
  @Path("/insert/pedidos/")
  public Response insert(@Valid PedidoDTO dto) {

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

  @POST
  @Path("post/telefone/")
  @RolesAllowed({"User"})
  public Response insertTelefoneUsuario(@Valid TelefoneDTO tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.status(200).entity(service.insertTelefoneUsuario(tel, idUsuario)).build();
  }

  @PUT
  @Path("put/telefone/")
  @RolesAllowed({"User"})
  public Response updateTelefoneUsuario(@Valid TelefonePatchDTO tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateTelefoneUsuario(tel, idUsuario)).build();
  }

  @PUT
  @Path("put/endereco/")
  @RolesAllowed({"User"})
  public Response updateEnderecoUsuario(@Valid EnderecoPatchDTO end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateEnderecoUsuario(end, idUsuario)).build();
  }

  @POST
  @Path("post/endereco/")
  @RolesAllowed({"User"})
  public Response insertEnderecoUsuario(@Valid EnderecoDTO end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.status(200).entity(service.insertEnderecoUsuario(end, idUsuario)).build();
  }

  @DELETE
  @Path("/delete/")
  @RolesAllowed({"User"})
  public Response deleteUsuario() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    service.deleteUsuario(idUsuario);
    return Response.ok().build();
  }

  @GET
  @Path("/search/listaDesejos/")
  @RolesAllowed({"User"})
  public Response findListaDesejosUsuario() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.ok(service.findListaDesejosUsuario(idUsuario)).build();
  }

  @DELETE
  @Path("/delete/pedido/")
  @RolesAllowed({"User"})
  public Response deletePedidoByUsuario(Long idPedido) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    servicePedido.deletePedidoByUsuario(idUsuario, idPedido);
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
  public Response updateEndereco(@Valid PedidoPatchEnderecoDTO dto) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.ok(servicePedido.updateEndereco(dto, idUsuario)).build();
  }

  @GET
  @Path("/search/pedidos/")
  @RolesAllowed({"User"})
  public Response findPedidoByUsuario() {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idUsuario = service.findByLogin(login).id();
    return Response.status(200).entity(servicePedido.findPedidoByUsuario(idUsuario)).build();
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
      return Response.status(Response.Status.CONFLICT).entity(error).build();
    }

    String login = jwt.getSubject();
    Long idUsuario = service.findByLogin(login).id();

    return Response.ok(fileService.updateNomeImagemC(idUsuario, nomeImagem)).build();

  }

  @GET
  @Path("/download/imagem/{nomeImagem}")
  @RolesAllowed({"User"})
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response download(@PathParam("nomeImagem") String nomeImagem){
    return Response
            .ok(fileService.obterU(nomeImagem), MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=" + nomeImagem)
            .build();

  }
}
