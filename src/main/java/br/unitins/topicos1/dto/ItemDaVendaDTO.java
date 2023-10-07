package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ItemDaVendaDTO(
    Long id,
    @NotBlank(message = "O campo nome não pode ser nulo.")
    @Pattern(regexp = "^[0-9]+[.,][0-9]*$", message = "Valor digitado inválido!")
    Double preco,
    @NotBlank(message = "O campo nome não pode ser nulo.")
    @Pattern(regexp = "[0-9]+", message = "Valor digitado inválido!")
    Long quantidade,
    @NotBlank(message = "O campo produto não pode ser nulo.")
    Produto produto
) {
    public static ItemDaVendaDTO valueOf(ItemDaVenda idv) {
        return new ItemDaVendaDTO(
            idv.getId(),
            idv.getPreco(),
            idv.getQuantidade(),
            idv.getProduto()
        );
    }
    
}
