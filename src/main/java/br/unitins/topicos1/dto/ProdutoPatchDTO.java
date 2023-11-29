package br.unitins.topicos1.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record ProdutoPatchDTO(

    @NotNull(message = "O campo Id não pode ser nulo")
    Long id,
    @NotNull(message = "O campo Valor da venda não pode ser nulo")
    @Digits(integer = 6, fraction = 3, message = "Por favor, digite um número válido")
    Double valorVenda,
    @NotNull(message = "O campo quantidade de produtos não pode ser nulo")
    @Digits(integer = 10, fraction = 0, message = "Por favor, digite um número válido")
    Integer quantidade,
    @NotNull(message = "O campo Lote não pode ser nulo")
    Long idLoteProduto
    ) {}
