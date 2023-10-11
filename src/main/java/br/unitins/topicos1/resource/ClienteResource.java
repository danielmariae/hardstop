package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClientePatchSenhaDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.service.ClienteService;
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

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

  @Inject
  ClienteService service;

  @POST
  @Transactional
  public Response insert(@Valid ClienteDTO dto) {
    return Response.status(201).entity(service.insert(dto)).build();
  }

  @PUT
  @Transactional
  @Path("put/{id}")
  public Response update(@Valid ClienteDTO dto, @PathParam("id") Long id) {
    service.update(dto, id);
    return Response.status(Status.NO_CONTENT).build();
  }

  @PATCH
  @Transactional
  @Path("patch/senha/")
  public Response updateSenha(@Valid ClientePatchSenhaDTO senha) {
    return Response.status(200).entity(service.updateSenha(senha)).build();
  }

  @PATCH
  @Transactional
  @Path("patch/telefone/{id}")
  public Response updateTelefone(
    @Valid List<TelefonePatchDTO> tel,
    @PathParam("id") Long id
  ) {
    return Response.status(200).entity(service.updateTelefone(tel, id)).build();
  }

  @PATCH
  @Transactional
  @Path("patch/endereco/{id}")
  public Response updateEndereco(
    @Valid List<EnderecoPatchDTO> end,
    @PathParam("id") Long id
  ) {
    return Response.status(200).entity(service.updateEndereco(end, id)).build();
  }

  @DELETE
  @Transactional
  @Path("/delete/{id}")
  public Response delete(@PathParam("id") Long id) {
    //service.delete(id);
    //return Response.status(Status.NO_CONTENT).build();
    return Response.ok(service.delete(id)).build();
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

 @GET
  @Path("/search/listaDesejos/{id}")
  public Response findListaDesejos(@PathParam("id") Long id) {
    return Response.ok(service.findListaDesejos(id)).build();
  }
}
