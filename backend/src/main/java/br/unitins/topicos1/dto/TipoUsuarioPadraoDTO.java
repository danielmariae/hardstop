package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.TipoUsuario;

public record TipoUsuarioPadraoDTO(
    LocalDateTime dataCriacao,
    Integer idTipoPerfil
) {
    public static TipoUsuarioDTO valueOf(TipoUsuario tipoUsuario){
        return new TipoUsuarioDTO(
            tipoUsuario.getDataCriacao(),
            0);
    }
}
