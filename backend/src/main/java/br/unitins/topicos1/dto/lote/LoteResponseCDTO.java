package br.unitins.topicos1.dto.lote;

import java.time.LocalDateTime;

import br.unitins.topicos1.dto.fornecedor.FornecedorResponseDTO;
import br.unitins.topicos1.dto.produto.ProdutoResponseDTO;
import br.unitins.topicos1.model.lote.Lote;

public record LoteResponseCDTO(
    Long id,
    String lote,
    LocalDateTime dataHoraChegadaLote,
    LocalDateTime dataHoraAtivacaoLote,
    LocalDateTime dataHoraUltimoVendido,
    FornecedorResponseDTO fornecedor,
    ProdutoResponseDTO produto,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    Double custoCompra,
    Double valorVenda,
    Integer garantiaMeses,
    StatusDoLoteDTO statusDoLote
    ) {
    public static LoteResponseCDTO valueOf(Lote lote){
        return new LoteResponseCDTO(
            lote.getId(),
            lote.getLote(),
            lote.getDataHoraChegadaLote(),
            lote.getDataHoraAtivacaoLote(),
            lote.getDataHoraUltimoVendido(),
            FornecedorResponseDTO.valueOf(lote.getFornecedor()),
            ProdutoResponseDTO.valueOf(lote.getProduto()),
            lote.getQuantidadeUnidades(),
            lote.getQuantidadeNaoConvencional(),
            lote.getUnidadeDeMedida(),
            lote.getCustoCompra(),
            lote.getValorVenda(),
            lote.getGarantiaMeses(),
            StatusDoLoteDTO.valueOf(lote.getStatusDoLote())
            );
    }
    }
