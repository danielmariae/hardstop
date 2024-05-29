package br.unitins.topicos1.dto.pedido.itemVenda;

import br.unitins.topicos1.model.pedido.ItemDaVenda;

public record ItemDaVendaResponseDTO(
    Long id,
    Double preco,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    String nome
) {
    public static ItemDaVendaResponseDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaResponseDTO(
            idv.getId(),
            idv.getPreco(),
            idv.getQuantidadeUnidades(),
            idv.getQuantidadeNaoConvencional(),
            idv.getUnidadeDeMedida(),
            idv.getProduto().getNome()
        );
    }
    
}