package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.Produto;
import jakarta.validation.constraints.Pattern;

public record ItemDaVendaDTO(

    @Pattern(regexp = "^[0-9]+[.,][0-9]*$", message = "Valor digitado inválido!")
    Double preco,
   
    @Pattern(regexp = "[0-9]+", message = "Valor digitado inválido!")
    Long quantidade,
   
    Produto produto
) {
    public static ItemDaVendaDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaDTO(
            idv.getPreco(),
            idv.getQuantidade(),
            idv.getProduto()
        );
    }
    
}
