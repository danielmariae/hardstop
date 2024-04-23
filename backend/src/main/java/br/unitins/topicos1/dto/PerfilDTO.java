package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Perfil;

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
