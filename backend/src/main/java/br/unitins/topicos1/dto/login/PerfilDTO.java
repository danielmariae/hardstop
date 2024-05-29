package br.unitins.topicos1.dto.login;

import br.unitins.topicos1.model.utils.Perfil;

public record PerfilDTO(
    Integer id, 
    String label
) {
    public static PerfilDTO valueOf(Perfil perfil){
        return new PerfilDTO(
            perfil.getId(), 
            perfil.getLabel()
        );
    }
}
