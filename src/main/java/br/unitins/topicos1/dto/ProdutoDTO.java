package br.unitins.topicos1.dto;

import java.util.List;

// import java.util.List;
import br.unitins.topicos1.model.Produto;

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
    Integer quantidade,
    List<LoteDTO> listaLote,
    ClassificacaoDTO classificacao) {
        public static ProdutoDTO valueOf(Produto produto) {
            return new ProdutoDTO(
                produto.getNome(),
                produto.getDescricao(),
                produto.getCodigoBarras(),
                produto.getMarca(),
                produto.getAltura(),
                produto.getLargura(),
                produto.getComprimento(),
                produto.getPeso(),
                produto.getCustoCompra(),
                produto.getValorVenda(),
                produto.getQuantidade(),
                produto
                .getListaLote()
                .stream()
                .map(l -> LoteDTO.valueOf(l))
                .toList(),
                ClassificacaoDTO.valueOf(produto.getClassificacao())
                );
        }
    }
