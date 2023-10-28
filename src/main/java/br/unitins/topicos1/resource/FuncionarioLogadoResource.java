package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.service.FuncionarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/funcionariologado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioLogadoResource {

@Inject
JsonWebToken jwt;

@Inject
FuncionarioService service;

@GET
@RolesAllowed({"Func", "Admin"})
public Response getFuncionario() {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();
    
    return Response.ok(service.findByLogin(login)).build();
}

@PATCH
  @Transactional
  @RolesAllowed({"Func", "Admin"})
  @Path("patch/senha/")
  public Response updateSenha(@Valid PatchSenhaDTO senha, Long idFuncionario) {

    // obtendo o login pelo token jwt
    String login = jwt.getSubject();

    Long id = service.findByLogin(login).id();

    return Response.status(200).entity(service.updateSenha(senha, id)).build();
  }
    
}
