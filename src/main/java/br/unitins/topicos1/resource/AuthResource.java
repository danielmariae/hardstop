package br.unitins.topicos1.resource;

import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.LoginDTO;
import br.unitins.topicos1.service.ClienteService;
import br.unitins.topicos1.service.FuncionarioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthResource {
@Inject
ClienteService serviceC;

@Inject
FuncionarioService serviceF;

@POST
public Response loginC(@Valid LoginDTO dto) {
    ClienteResponseDTO result = serviceC.findByLoginAndSenha(dto.login(), dto.senha());
    return Response.ok().entity(result).build();
}

@POST
public Response loginF(@Valid LoginDTO dto) {
    FuncionarioResponseDTO result = serviceF.findByLoginAndSenha(dto.login(), dto.senha());
    return Response.ok().entity(result).build();
}
}
