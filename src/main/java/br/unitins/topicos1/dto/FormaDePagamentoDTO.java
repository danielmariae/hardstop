package br.unitins.topicos1.dto;

public record FormaDePagamentoDTO(
    // Modalidade (1 para CartaoDeCredito, 2 para Boleto e 3 para Pix)
    Integer modalidade,
    // Dados para Cartao de Credito
    String numeroCartao,
    Integer mesValidade,
    Integer anoValidade,
    Integer codSeguranca,
    // Dados para Boleto
    //String nomeBanco,
    Integer diasVencimento
    // Dados para Pix
    //String nomeCliente
    //String nomeRecebedor,
    //String chaveRecebedor,
    //String nomeCidade
) {
    }
