package br.unitins.topicos1.dto;


import java.util.List;

import br.unitins.topicos1.model.Classificacao;
import br.unitins.topicos1.model.Produto;

public record ProdutoResponseDTO(     
    Long id,
    String nome,
    String modelo,
    String descricao,
    String codigoBarras,
    String marca,
    Double altura,
    Double largura,
    Double comprimento,
    Double peso,
    Double valorVenda,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    Classificacao classificacao,
    List<String> nomeImagem, 
    String imagemPrincipal
    ) {
        public static ProdutoResponseDTO valueOf(Produto produto){
            return new ProdutoResponseDTO(
                produto.getId(), 
                produto.getNome(),
                produto.getModelo(), 
                produto.getDescricao(), 
                produto.getCodigoBarras(), 
                produto.getMarca(), 
                produto.getAltura(), 
                produto.getLargura(), 
                produto.getComprimento(), 
                produto.getPeso(), 
                produto.getValorVenda(), 
                produto.getQuantidadeUnidades(),
                produto.getQuantidadeNaoConvencional(),
                produto.getUnidadeDeMedida(),
                produto.getClassificacao(),
                produto.getNomeImagem(),
                produto.getImagemPrincipal()
            );
        }
        
    }
