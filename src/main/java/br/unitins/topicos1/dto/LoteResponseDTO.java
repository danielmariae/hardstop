package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;

public record LoteResponseDTO(
    Long id,
    String lote,
    Fornecedor fornecedor
) {
    public static LoteResponseDTO valueOf(Lote lote){
        return new LoteResponseDTO(
            lote.getId(),
            lote.getLote(),
            lote.getFornecedor()
            );
    }
}
