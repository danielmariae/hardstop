package br.unitins.topicos1.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoDTO(

    @NotBlank(message = "O campo Nome não pode ser nulo.")
    String nome,
    @NotBlank(message = "O campo Modelo não pode ser nulo.")
    String modelo,
    @NotBlank(message = "O campo CPU não pode ser nulo.")
    String cpu,
    @NotBlank(message = "O campo CHIPSET não pode ser nulo.")
    String chipset,
    @NotBlank(message = "O campo Memoria não pode ser nulo.")
    String memoria,
    @NotBlank(message = "O campo BIOS não pode ser nulo.")
    String bios,
    @NotBlank(message = "O campo Grafico não pode ser nulo.")
    String grafico,
    @NotBlank(message = "O campo Lan não pode ser nulo.")
    String lan,
    @NotBlank(message = "O campo Slots não pode ser nulo.")
    String slots,
    @NotBlank(message = "O campo Armazenamento não pode ser nulo.")
    String armazenamento,
    @NotBlank(message = "O campo Marca não pode ser nulo.")
    String marca,
    @NotBlank(message = "O campo Descricao não pode ser nulo.")
    String descricao,
    @NotBlank(message = "O campo Codigo de Barras do produto não pode ser nulo.")
    String codigoBarras,
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
