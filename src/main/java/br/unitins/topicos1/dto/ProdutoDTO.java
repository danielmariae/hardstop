package br.unitins.topicos1.dto;

public record ProdutoDTO(     
    String nome,
    String descricao,
    String codigoBarras,
    String marca,
    Double altura,
    Double largura,
    Double comprimento,
    Double peso,
    Double custoCompra,
    Double valorVenda,
    Long quantidade) {}
