package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Produto;

public record ProdutoResponseDTO(     
    Long id,
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
    Long quantidade) {
        public static ProdutoResponseDTO valueOf(Produto produto){
            return new ProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getCodigoBarras(), produto.getMarca(), produto.getAltura(), produto.getLargura(), produto.getComprimento(), produto.getPeso(), produto.getCustoCompra(), produto.getValorVenda(), produto.getQuantidade());
        }
    }
