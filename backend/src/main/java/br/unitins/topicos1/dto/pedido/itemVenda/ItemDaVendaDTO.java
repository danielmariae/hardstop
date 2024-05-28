package br.unitins.topicos1.dto.pedido.itemVenda;

import br.unitins.topicos1.model.pedido.ItemDaVenda;


public record ItemDaVendaDTO(

    Double preco,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    Long idProduto
    /* ProdutoDTO produto */
) {
    public static ItemDaVendaDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaDTO(
            idv.getPreco(),
            idv.getQuantidadeUnidades(),
            idv.getQuantidadeNaoConvencional(),
            idv.getUnidadeDeMedida(),
            idv.getProduto().getId()
            /* ProdutoDTO.valueOf(idv.getProduto()) */
        );
    }
    
}
