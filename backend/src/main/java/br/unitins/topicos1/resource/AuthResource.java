// Crtl K e depois Crtl U para descomentar
// Crtl A, depois Ctrl K e depois Crtl C

package br.unitins.topicos1.resource;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.LoginDTO;
import br.unitins.topicos1.service.ClienteService;
import br.unitins.topicos1.service.FuncionarioService;
import br.unitins.topicos1.service.HashService;
import br.unitins.topicos1.service.JwtService;
import jakarta.annotation.security.PermitAll;
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

    @Inject
    HashService hashService;

    @Inject
    JwtService jwtService;

    @Inject
    JsonWebToken jwt;

    @PermitAll
    @POST
    @Path("loginU/")
    public Response loginU(@Valid LoginDTO dto) {
        String hashSenha = hashService.getHashSenha(dto.senha());

        ClienteResponseDTO result = serviceC.findByLoginAndSenha(dto.login(), hashSenha);

        String token = jwtService.generateJwt(result);
        //System.out.println(token);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return Response.ok(responseBody).build();

        //return Response.ok().header("Authorization", token).build();
    }

    @PermitAll
    @POST
    @Path("loginF/")
    public Response loginF(@Valid LoginDTO dto) {
        
        String hashSenha = hashService.getHashSenha(dto.senha());
    
        FuncionarioResponseDTO result = serviceF.findByLoginAndSenha(dto.login(), hashSenha);
    
        String token = jwtService.generateJwt(result);

        //System.out.println(token);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return Response.ok(responseBody).build();
    
        //return Response.ok().header("Authorization", token).build();
        
    }
}
