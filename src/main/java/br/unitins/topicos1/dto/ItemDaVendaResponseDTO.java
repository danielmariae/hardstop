package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.ItemDaVenda;

public record ItemDaVendaResponseDTO(
    Long id,
    Double preco,
    Long quantidade,
    ProdutoResponseDTO produto
) {
    public static ItemDaVendaResponseDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaResponseDTO(
            idv.getId(),
            idv.getPreco(),
            idv.getQuantidade(),
            ProdutoResponseDTO.valueOf(idv.getProduto())
        );
    }
    
}