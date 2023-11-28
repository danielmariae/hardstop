package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoFornecedorPatch(
    @NotNull(message = "idProduto não poder ser vazio ou nulo.")
    Long idProduto,
    @NotBlank(message = "Inserir data e hora no formato YYYY-MM-DD hh:mm:ss.")
    String dataHoraVenda
) {
    
}
