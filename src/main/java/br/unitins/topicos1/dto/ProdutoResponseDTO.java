package br.unitins.topicos1.dto;

import java.util.List;

// import java.util.List;

import br.unitins.topicos1.model.Classificacao;
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
    Double valorVenda,
    Integer quantidade,
    Long idlote,
    List<String> nomeImagem,
    Classificacao classificacao
    ) {
        public static ProdutoResponseDTO valueOf(Produto produto){
            return new ProdutoResponseDTO(
                produto.getId(), 
                produto.getNome(), 
                produto.getDescricao(), 
                produto.getCodigoBarras(), 
                produto.getMarca(), 
                produto.getAltura(), 
                produto.getLargura(), 
                produto.getComprimento(), 
                produto.getPeso(), 
                produto.getValorVenda(), 
                produto.getQuantidade(),
                produto.getLoteAtual().getId(),
                produto.getNomeImagem(),
                produto.getClassificacao()      
            );
        }
    }
