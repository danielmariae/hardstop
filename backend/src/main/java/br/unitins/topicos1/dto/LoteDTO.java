package br.unitins.topicos1.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteDTO(
    @NotBlank(message = "O campo lote não pode ser nulo.")
    String lote,
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Long idFornecedor,
    @NotNull(message = "O campo idProduto não pode ser nulo")
    Long idProduto,
    //@NotNull(message = "O campo quantidade não pode ser nulo")
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    @NotNull(message = "O campo Custo da compra não pode ser nulo")
    @Digits(integer = 6, fraction = 3, message = "Por favor, digite um número válido")
    Double custoCompra,
    @NotNull(message = "O campo Valor da venda não pode ser nulo")
    @Digits(integer = 6, fraction = 3, message = "Por favor, digite um número válido")
    Double valorVenda
) {}
