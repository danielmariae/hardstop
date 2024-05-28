package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.unitins.topicos1.application.ErrorTP1;
import br.unitins.topicos1.dto.cliente.ClienteDTO;
import br.unitins.topicos1.dto.cliente.ClienteNSDTO;
import br.unitins.topicos1.dto.endereco.EnderecoDTO;
import br.unitins.topicos1.dto.endereco.EnderecoPatchDTO;
import br.unitins.topicos1.dto.patch.PatchCpfDTO;
import br.unitins.topicos1.dto.patch.PatchEmailDTO;
import br.unitins.topicos1.dto.patch.PatchLoginDTO;
import br.unitins.topicos1.dto.patch.PatchNomeDTO;
import br.unitins.topicos1.dto.patch.PatchSenhaDTO;
import br.unitins.topicos1.dto.telefone.TelefoneDTO;
import br.unitins.topicos1.dto.telefone.TelefonePatchDTO;
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
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

@Inject
JsonWebToken jwt;

@Inject
ClienteService service;

@Inject
PedidoService servicePedido;

@Inject
FileService fileService;

  @POST
  @Transactional
  public Response insertCliente(@Valid ClienteDTO dto) {
    return Response.status(201).entity(service.insertCliente(dto)).build();
  }

  @GET
  @RolesAllowed({"User"})
  @Path("this")
  public Response getCliente() {
      // obtendo o login pelo token jwt
      String login = jwt.getSubject();
      
      return Response.ok(service.findByLogin(login)).build();
  }

  @GET
  @RolesAllowed({"User"})
  @Path("this/perfil")
  public Response getClientePerfil() {
      // obtendo o login pelo token jwt
      String login = jwt.getSubject();
      
      return Response.ok(service.findPerfilByLogin(login)).build();
  }


  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/nome/")
  public Response updateNome(@Valid PatchNomeDTO nome, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    UpdateResponse entity = new UpdateResponse(service.updateNome(nome, id));

    return Response.status(200).entity(entity).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/cpf/")
  public Response updateCpf(@Valid PatchCpfDTO cpf, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    UpdateResponse entity = new UpdateResponse(service.updateCpf(cpf, id));

    return Response.status(200).entity(entity).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/login/")
  public Response updateLogin(@Valid PatchLoginDTO novoLogin, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    UpdateResponse entity = new UpdateResponse(service.updateLogin(novoLogin, id));

    return Response.status(200).entity(entity).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/email/")
  public Response updateEmail(@Valid PatchEmailDTO email, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    UpdateResponse entity = new UpdateResponse(service.updateEmail(email, id));

    return Response.status(200).entity(entity).build();
  }

  @PATCH
  @RolesAllowed({"User"})
  @Path("patch/senha/")
  public Response updateSenha(@Valid PatchSenhaDTO senha, Long idCliente) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    UpdateResponse entity = new UpdateResponse(service.updateSenhaCliente(senha, id));

    return Response.status(200).entity(entity).build();
  
    
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
  public Response insertTelefoneCliente(@Valid TelefoneDTO tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.insertTelefoneCliente(tel, idCliente)).build();
  }

  @PUT
  @Path("put/telefone/")
  @RolesAllowed({"User"})
  public Response updateTelefoneCliente(@Valid TelefonePatchDTO tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateTelefoneCliente(tel, idCliente)).build();
  }

  @PUT
  @Path("put/endereco/")
  @RolesAllowed({"User"})
  public Response updateEnderecoCliente(@Valid EnderecoPatchDTO end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateEnderecoCliente(end, idCliente)).build();
  }

  @PUT
  @Path("this")
  @RolesAllowed({"User"})
  public Response updateThis(@Valid ClienteNSDTO cliente) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.updateClienteNS(cliente, idCliente)).build();
  }


  @POST
  @Path("post/endereco/")
  @RolesAllowed({"User"})
  public Response insertEnderecoCliente(@Valid EnderecoDTO end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = service.findByLogin(login).id();
    return Response.status(200).entity(service.insertEnderecoCliente(end, idCliente)).build();
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
  @Path("/delete")
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
    return Response
    .ok(fileService.obterU(nomeImagem), MediaType.APPLICATION_OCTET_STREAM)
    .header("Content-Disposition", "attachment; filename=" + nomeImagem)
    .build();

  }

  @DELETE
  @Transactional
  @Path("/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response deleteById(@PathParam("id") Long id) {
    service.deleteCliente(id);
    return Response.status(Status.NO_CONTENT).build();
  }

  
  @GET
  @Path("/all")
  @RolesAllowed({"Func", "Admin"})
  public Response findbyAll() {
    return Response.ok(service.findByAllCliente()).build();
  }

  @GET
  @RolesAllowed({"Func", "Admin"})
  public Response findAll(
      @QueryParam("page") @DefaultValue("0") int page,
      @QueryParam("pageSize") @DefaultValue("100") int pageSize) {

      return Response.ok(service.findByAll(page, pageSize)).build();
  }

  @GET
  @Path("/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response findById(@PathParam("id") Long id) {
    return Response.ok(service.findByIdCliente(id)).build();
  }

  @GET
  @Path("/count")
  public Long count() {
      return service.count();
  }

  @GET
  @Path("/nome/{nome}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByName(@PathParam("nome") String nome) {
    return Response.ok(service.findByNameCliente(nome)).build();
  }

  @GET
  @Path("/cpf/{cpf}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByCpfCliente(@PathParam("cpf") String cpf) {
    return Response.ok(service.findByCpfCliente(cpf)).build();
  }

  @PUT
  @Transactional
  @Path("/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response updateCliente(@Valid ClienteDTO dto, @PathParam("id") Long id) {
    service.updateCliente(dto, id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @PUT
  @Transactional
  @Path("ns/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response updateClienteNS(@Valid ClienteNSDTO dto, @PathParam("id") Long id) {
    service.updateClienteNS(dto, id);
    return Response.status(Status.NO_CONTENT).build();
  }
}

class UpdateResponse {
  private String message;

  @JsonCreator
  public UpdateResponse(@JsonProperty("message") String message) {
    this.message = message;
  }  

  public String getMessage() {
      return message;
  }
}