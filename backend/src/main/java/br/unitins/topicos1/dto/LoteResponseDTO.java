package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Lote;

public record LoteResponseDTO(
    Long id,
    String lote,
    LocalDateTime dataHoraChegadaLote,
    LocalDateTime dataHoraAtivacaoLote,
    LocalDateTime dataHoraUltimoVendido,
    String cnpjFornecedor,
    String modeloProduto,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    Double custoCompra,
    Double valorVenda,
    Integer garantiaMeses,
    String descricaoStatusDoLote
) {
    public static LoteResponseDTO valueOf(Lote lote){
        return new LoteResponseDTO(
            lote.getId(),
            lote.getLote(),
            lote.getDataHoraChegadaLote(),
            lote.getDataHoraAtivacaoLote(),
            lote.getDataHoraUltimoVendido(),
            lote.getFornecedor().getCnpj(),
            lote.getProduto().getModelo(),
            lote.getQuantidadeUnidades(),
            lote.getQuantidadeNaoConvencional(),
            lote.getUnidadeDeMedida(),
            lote.getCustoCompra(),
            lote.getValorVenda(),
            lote.getGarantiaMeses(),
            lote.getStatusDoLote().getDescricao()
            );
    }
}