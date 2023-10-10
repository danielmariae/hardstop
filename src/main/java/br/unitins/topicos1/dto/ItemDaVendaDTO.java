package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.ItemDaVenda;


public record ItemDaVendaDTO(

    Double preco,
    Integer quantidade,
    Long idProduto
    /* ProdutoDTO produto */
) {
    public static ItemDaVendaDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaDTO(
            idv.getPreco(),
            idv.getQuantidade(),
            idv.getIdProduto()
            /* ProdutoDTO.valueOf(idv.getProduto()) */
        );
    }
    
}
