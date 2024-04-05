package br.unitins.topicos1.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.dto.StatusDoLoteDTO;
import br.unitins.topicos1.dto.TipoTelefoneDTO;
import br.unitins.topicos1.dto.UnidadeFederativaDTO;
import br.unitins.topicos1.model.StatusDoLote;
import br.unitins.topicos1.model.TipoTelefone;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/enum")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll

public class EnviarModelo {
  
    @GET
    @Path("/tipoTelefone")
    public List<TipoTelefoneDTO> obterTipoTelefone() {
        return Arrays.asList(TipoTelefone.values())
            .stream()
            .map(tipoTelefone -> TipoTelefoneDTO.valueOf(tipoTelefone))
            .collect(Collectors.toList());
    }

    @GET
    @Path("/uf")
    public List<UnidadeFederativaDTO> obterUF() {
        return Arrays.asList(UnidadeFederativa.values())
            .stream()
            .map(uf -> UnidadeFederativaDTO.valueOf(uf))
            .collect(Collectors.toList());
    }

    @GET
    @Path("/statusDoLote")
    public List<StatusDoLoteDTO> obtertStatusDoLote() {
        return Arrays.asList(StatusDoLote.values())
            .stream()
            .map(statusDoLote -> StatusDoLoteDTO.valueOf(statusDoLote))
            .collect(Collectors.toList());
    }
}
