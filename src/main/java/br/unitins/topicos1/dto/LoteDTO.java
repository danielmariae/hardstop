package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;

public record LoteDTO(
    String lote,
    Fornecedor fornecedor
) {
    public static LoteDTO valueOf(Lote lote)
    {
        return new LoteDTO(
            lote.getLote(),
            lote.getFornecedor()
        );
    } 
}
