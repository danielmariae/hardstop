package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Classificacao;
import br.unitins.topicos1.model.Lote;

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
    Long quantidade,
    List<Lote> listaLote,
    List<Classificacao> listaClassificacao) {}
