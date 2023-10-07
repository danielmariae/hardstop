package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Lote;

public record LoteDTO(
    String lote
) {
    public static LoteDTO valueOf(Lote lote)
    {
        return new LoteDTO(
            lote.getLote()
        );
    } 
}
