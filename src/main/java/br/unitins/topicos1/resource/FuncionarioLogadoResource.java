package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.application.ErrorTP1;
import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.EnderecoFuncDTO;
import br.unitins.topicos1.dto.EnderecoFuncPatchDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.model.form.ArchiveForm;
import br.unitins.topicos1.service.ClienteService;
import br.unitins.topicos1.service.FileService;
import br.unitins.topicos1.service.FuncionarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/funcionariologado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioLogadoResource {

@Inject
JsonWebToken jwt;

@Inject
private FuncionarioService serviceFuncionario;

@Inject
private ClienteService serviceCliente;

@Inject
FileService fileService;

@GET
@RolesAllowed({"Func", "Admin"})
public Response getFuncionario() {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();
    
    return Response.ok(serviceFuncionario.findByLogin(login)).build();
}

  @PATCH
  @RolesAllowed({"Func", "Admin"})
  @Path("patch/nome/")
  public Response updateNome(@Valid PatchNomeDTO nome, Long idFuncionario) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = serviceFuncionario.findByLogin(login).id();

    return Response.status(200).entity(serviceFuncionario.updateNome(nome, id)).build();
  }

  @PATCH
  @RolesAllowed({"Func", "Admin"})
  @Path("patch/cpf/")
  public Response updateCpf(@Valid PatchCpfDTO cpf, Long idFuncionario) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = serviceFuncionario.findByLogin(login).id();

    return Response.status(200).entity(serviceFuncionario.updateCpf(cpf, id)).build();
  }

  @PATCH
  @RolesAllowed({"Func", "Admin"})
  @Path("patch/login/")
  public Response updateLogin(@Valid PatchLoginDTO novoLogin, Long idFuncionario) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = serviceFuncionario.findByLogin(login).id();

    return Response.status(200).entity(serviceFuncionario.updateLogin(novoLogin, id)).build();
  }

  @PATCH
  @RolesAllowed({"Func", "Admin"})
  @Path("patch/email/")
  public Response updateEmail(@Valid PatchEmailDTO email, Long idFuncionario) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = serviceFuncionario.findByLogin(login).id();

    return Response.status(200).entity(serviceFuncionario.updateEmail(email, id)).build();
  }

  @PATCH
  @Transactional
  @RolesAllowed({"Func", "Admin"})
  @Path("patch/senha/")
  public Response updateSenhaFuncionario(@Valid PatchSenhaDTO senha, Long idFuncionario) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = serviceFuncionario.findByLogin(login).id();

    return Response.status(200).entity(serviceFuncionario.updateSenhaFuncionario(senha, id)).build();
  }

  @GET
  @Path("/search/cliente/all")
  @RolesAllowed({"Func", "Admin"})
  public Response findbyAllCliente() {
    return Response.ok(serviceCliente.findByAllCliente()).build();
  }

  @GET
  @Path("/search/cliente/id/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByIdCliente(@PathParam("id") Long id) {
    return Response.ok(serviceCliente.findByIdCliente(id)).build();
  }

  @GET
  @Path("/search/cliente/nome/{nome}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByNameCliente(@PathParam("nome") String nome) {
    return Response.ok(serviceCliente.findByNameCliente(nome)).build();
  }

  @GET
  @Path("/search/cliente/cpf/{cpf}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByCpfCliente(@PathParam("cpf") String cpf) {
    return Response.ok(serviceCliente.findByCpfCliente(cpf)).build();
  }

  @PUT
  @Transactional
  @Path("put/cliente/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response updateCliente(@Valid ClienteDTO dto, @PathParam("id") Long id) {
    serviceCliente.updateCliente(dto, id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @POST
  @Transactional
  @Path("/insert/funcionario")
  @RolesAllowed("Admin")
  public Response insertFuncionario(@Valid FuncionarioDTO dto) {
    FuncionarioDTO retorno =  serviceFuncionario.insertFuncionario(dto);
    return Response.status(201).entity(retorno).build();
  }

  @PUT
  @Transactional
  @RolesAllowed("Admin")
  @Path("put/funcionario/{id}")
  public Response updateFuncionario(@Valid FuncionarioDTO dto, @PathParam("id") Long id) {
    serviceFuncionario.updateFuncionario(dto, id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @POST
  @Path("insert/telefone/")
  @RolesAllowed({"Func", "Admin"})
  public Response insertTelefoneFuncionario(@Valid TelefoneDTO tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = serviceFuncionario.findByLogin(login).id();
    return Response.status(200).entity(serviceFuncionario.insertTelefoneFuncionario(tel, idCliente)).build();
  }

  @PUT
  @Path("put/telefone/")
  @RolesAllowed({"Func", "Admin"})
  public Response updateTelefoneFuncionario(@Valid TelefonePatchDTO tel) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = serviceFuncionario.findByLogin(login).id();
    return Response.status(200).entity(serviceFuncionario.updateTelefoneFuncionario(tel, idCliente)).build();
  }

  @PUT
  @Path("put/endereco/")
  @RolesAllowed({"Func", "Admin"})
  public Response updateEnderecoFuncionario(@Valid EnderecoFuncPatchDTO end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = serviceFuncionario.findByLogin(login).id();
    return Response.status(200).entity(serviceFuncionario.updateEnderecoFuncionario(end, idCliente)).build();
  }

  @POST
  @Path("insert/endereco/")
  @RolesAllowed({"Func", "Admin"})
  public Response insertEnderecoFuncionario(@Valid EnderecoFuncDTO end) {
    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long idCliente = serviceFuncionario.findByLogin(login).id();
    return Response.status(200).entity(serviceFuncionario.insertEnderecoFuncionario(end, idCliente)).build();
  }

  @DELETE
  @Transactional
  @Path("/delete/funcionario/{id}")
  @RolesAllowed("Admin")
  public Response deleteFuncionario(@PathParam("id") Long id) {
    serviceFuncionario.deleteFuncionario(id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @DELETE
  @Transactional
  @Path("/delete/cliente/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response deleteCliente(@PathParam("id") Long id) {
    serviceCliente.deleteCliente(id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @GET
  @Path("/search/funcionario/all")
  @RolesAllowed({"Func", "Admin"})
  public Response findByAllFuncionario() {
    return Response.ok(serviceFuncionario.findByAllFuncionario()).build();
  }

  @GET
  @Path("/search/funcionario/id/{id}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByIdFuncionario(@PathParam("id") Long id) {
    return Response.ok(serviceFuncionario.findByIdFuncionario(id)).build();
  }

  @GET
  @Path("/search/funcionario/nome/{nome}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByNameFuncionario(@PathParam("nome") String nome) {
    return Response.ok(serviceFuncionario.findByNameFuncionario(nome)).build();
  }

  @GET
  @Path("/search/funcionario/cpf/{cpf}")
  @RolesAllowed({"Func", "Admin"})
  public Response findByCpfFuncionario(@PathParam("cpf") String cpf) {
    return Response.ok(serviceFuncionario.findByCpfFuncionario(cpf)).build();
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
        Long idFuncionario = serviceFuncionario.findByLogin(login).id();

        return Response.ok(fileService.updateNomeImagemF(idFuncionario, nomeImagem)).build();

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
