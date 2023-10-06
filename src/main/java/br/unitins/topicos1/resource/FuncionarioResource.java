package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.service.FuncionarioService;
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

import java.util.List;

@Path("/funcionarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioResource {

  @Inject
  FuncionarioService service;

  @POST
  @Transactional
  public Response insert(@Valid FuncionarioDTO dto) {
    FuncionarioResponseDTO retorno =  service.insert(dto);
    return Response.status(201).entity(retorno).build();
  }

  @PUT
  @Transactional
  @Path("put/{id}")
  public Response update(@Valid FuncionarioDTO dto, @PathParam("id") Long id) {
    service.update(dto, id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @PATCH
  @Transactional
  @Path("patch/senha/{id}")
  public Response updateSenha(@Valid String senha, @PathParam("id") Long id) {
    return Response.status(200).entity(service.updateSenha(senha, id)).build();
  }

  @PATCH
  @Transactional
  @Path("patch/telefone/{id}")
  public Response updateTelefone(@Valid List<TelefoneDTO> tel, @PathParam("id") Long id) {
    return Response.status(200).entity(service.updateTelefone(tel, id)).build();
  }

  @PATCH
  @Transactional
  @Path("patch/endereco/{id}")
  public Response updateEndereco(@Valid EnderecoDTO end,@PathParam("id") Long id) {
    return Response.status(200).entity(service.updateEndereco(end, id)).build();
  }

  @DELETE
  @Transactional
  @Path("/delete/{id}")
  public Response delete(@PathParam("id") Long id) {
    service.delete(id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @GET
  public Response findAll() {
    return Response.ok(service.findByAll()).build();
  }

  @GET
  @Path("/search/id/{id}")
  public Response findById(@PathParam("id") Long id) {
    return Response.ok(service.findById(id)).build();
  }

  @GET
  @Path("/search/nome/{nome}")
  public Response findByName(@PathParam("nome") String nome) {
    return Response.ok(service.findByName(nome)).build();
  }

  @GET
  @Path("/search/cpf/{cpf}")
  public Response findByCpf(@PathParam("cpf") String cpf) {
    return Response.ok(service.findByCpf(cpf)).build();
  }

}
