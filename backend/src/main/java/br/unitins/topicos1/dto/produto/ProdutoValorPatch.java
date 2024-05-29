package br.unitins.topicos1.dto.produto;

import jakarta.validation.constraints.NotNull;

public record ProdutoValorPatch(
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Long id,
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Double valor
) {
    
}
