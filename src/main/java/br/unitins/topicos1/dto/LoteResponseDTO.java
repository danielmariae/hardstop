package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;
import br.unitins.topicos1.model.Produto;

public record LoteResponseDTO(
    Long id,
    String lote,
    Fornecedor fornecedor,
    Produto produto,
    Integer quantidade,
    Double custoCompra,
    Double valorVenda
) {
    public static LoteResponseDTO valueOf(Lote lote){
        return new LoteResponseDTO(
            lote.getId(),
            lote.getLote(),
            lote.getFornecedor(),
            lote.getProduto(),
            lote.getQuantidade(),
            lote.getCustoCompra(),
            lote.getValorVenda()
            );
    }
}