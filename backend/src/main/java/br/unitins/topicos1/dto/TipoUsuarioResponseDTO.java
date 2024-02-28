package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Perfil;
import br.unitins.topicos1.model.TipoUsuario;

public record TipoUsuarioResponseDTO(
    LocalDateTime dataCriacao,
    Perfil perfil
) {
    public static TipoUsuarioResponseDTO valueOf(TipoUsuario tipoUsuario){
        return new TipoUsuarioResponseDTO(tipoUsuario.getDataCriacao(), tipoUsuario.getPerfil());
    }
}
