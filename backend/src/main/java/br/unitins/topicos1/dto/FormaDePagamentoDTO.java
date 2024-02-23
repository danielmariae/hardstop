package br.unitins.topicos1.dto;

import jakarta.validation.constraints.Digits;

public record FormaDePagamentoDTO(
    // Modalidade (1 para CartaoDeCredito, 2 para Boleto e 3 para Pix)
    @Digits(integer = 1, fraction = 0, message = "Por favor, digite um número válido")
    Integer modalidade,
    // Dados para Cartao de Credito
    String numeroCartao,
    @Digits(integer = 2, fraction = 0, message = "Por favor, digite um número válido")
    Integer mesValidade,
    @Digits(integer = 2, fraction = 0, message = "Por favor, digite um número válido")
    Integer anoValidade,
    @Digits(integer = 3, fraction = 0, message = "Por favor, digite um número válido")
    Integer codSeguranca,
    // Dados para Boleto
    @Digits(integer = 2, fraction = 0, message = "Por favor, digite um número válido")
    Integer diasVencimento
) {
    }
