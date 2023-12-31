package br.unitins.topicos1.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoDTO(

    @NotBlank(message = "O campo Nome não pode ser nulo.")
    String nome,
    @NotBlank(message = "O campo Descricao não pode ser nulo.")
    String descricao,
    @NotBlank(message = "O campo Codigo de Barras do produto não pode ser nulo.")
    String codigoBarras,
    @NotBlank(message = "O campo Marca não pode ser nulo.")
    String marca,
    @NotNull(message = "O campo altura não pode ser nulo")
    @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double altura,
    @NotNull(message = "O campo largura não pode ser nulo")
    @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double largura,
    @NotNull(message = "O campo comprimento não pode ser nulo")
    @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double comprimento,
    @NotNull(message = "O campo Peso do produto não pode ser nulo")
    @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double peso,
    @NotNull(message = "O campo Classificacao não pode ser nulo")
    Long idClassificacao
    ) {}
