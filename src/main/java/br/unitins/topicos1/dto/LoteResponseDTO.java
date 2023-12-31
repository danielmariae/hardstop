package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Lote;

public record LoteResponseDTO(
    Long id,
    String lote,
    Long idFornecedor,
    Long idProduto,
    Integer quantidade,
    Double custoCompra,
    Double valorVenda
) {
    public static LoteResponseDTO valueOf(Lote lote){
        return new LoteResponseDTO(
            lote.getId(),
            lote.getLote(),
            lote.getFornecedor().getId(),
            lote.getProduto().getId(),
            lote.getQuantidade(),
            lote.getCustoCompra(),
            lote.getValorVenda()
            );
    }
}