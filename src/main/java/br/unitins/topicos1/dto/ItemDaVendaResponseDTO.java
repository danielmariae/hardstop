package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.ItemDaVenda;

public record ItemDaVendaResponseDTO(
    Long id,
    Double preco,
    Integer quantidade,
    String nome
) {
    public static ItemDaVendaResponseDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaResponseDTO(
            idv.getId(),
            idv.getPreco(),
            idv.getQuantidade(),
            idv.getProduto().getNome()
        );
    }
    
}