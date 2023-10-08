package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.ItemDaVenda;


public record ItemDaVendaDTO(

    Double preco,
    Long quantidade
    /* ProdutoDTO produto */
) {
    public static ItemDaVendaDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaDTO(
            idv.getPreco(),
            idv.getQuantidade()
            /* ProdutoDTO.valueOf(idv.getProduto()) */
        );
    }
    
}
