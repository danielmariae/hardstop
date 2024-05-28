package br.unitins.topicos1.dto.lote;

import br.unitins.topicos1.model.lote.StatusDoLote;

public record StatusDoLoteDTO(
    Integer id,
    String descricao
) {
    public static StatusDoLoteDTO valueOf(StatusDoLote sl) {
        return new StatusDoLoteDTO(
            sl.getId(),
            sl.getDescricao()
            );
    }
}
