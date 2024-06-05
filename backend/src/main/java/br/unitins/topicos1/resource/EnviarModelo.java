package br.unitins.topicos1.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.dto.UnidadeFederativaDTO;
import br.unitins.topicos1.dto.login.PerfilDTO;
import br.unitins.topicos1.dto.lote.StatusDoLoteDTO;
import br.unitins.topicos1.dto.pedido.formaPagamento.ModalidadePagamentoDTO;
import br.unitins.topicos1.dto.pedido.status.StatusDTO;
import br.unitins.topicos1.dto.telefone.TipoTelefoneDTO;
import br.unitins.topicos1.model.lote.StatusDoLote;
import br.unitins.topicos1.model.pagamento.ModalidadePagamento;
import br.unitins.topicos1.model.pedido.Status;
import br.unitins.topicos1.model.utils.Perfil;
import br.unitins.topicos1.model.utils.TipoTelefone;
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
    @Path("/statusPedido")
    public List<StatusDTO> obterStatusPedido() {
        return Arrays.asList(Status.values())
        .stream()
        .map(status -> StatusDTO.valueOf(status))
        .collect(Collectors.toList());
    }

    @GET
    @Path("/modalidadePagamento")
    public List<ModalidadePagamentoDTO> obterModalidadePagamento() {
        return Arrays.asList(ModalidadePagamento.values())
            .stream()
            .map(modalidadePagamento -> ModalidadePagamentoDTO.valueOf(modalidadePagamento))
            .collect(Collectors.toList());
    }
  
  
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

    @GET
    @Path("/perfil")
    public List<PerfilDTO> obterPerfil() {
        return Arrays.asList(Perfil.values())
            .stream()
            .map(perfil -> PerfilDTO.valueOf(perfil))
            .collect(Collectors.toList());
    }
}
