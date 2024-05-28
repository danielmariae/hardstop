package br.unitins.topicos1.dto.telefone;

import br.unitins.topicos1.model.utils.TipoTelefone;

public record TipoTelefoneDTO(
    Integer id,
    String descricao
) {
   public static TipoTelefoneDTO valueOf(TipoTelefone tp) {
    return new TipoTelefoneDTO(
        tp.getId(),
        tp.getDescricao()
        );
   } 
}
