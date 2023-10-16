package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.CartaoDeCredito;

public record CartaoDeCreditoDTO(
    String numeroCartao,
    Integer mesValidade,
    Integer anoValidade,
    Integer codSeguranca,
    Double valorPago,
    LocalDateTime dataHoraPagamento,
    LocalDateTime dataHoraEfetivadoPagamento
) {
    public static CartaoDeCreditoDTO valueOf(CartaoDeCredito cc) {
        return new CartaoDeCreditoDTO(
            cc.getNumeroCartao(),
            cc.getMesValidade(),
            cc.getAnoValidade(),
            cc.getCodSeguranca(),
            cc.getValorPago(),
            cc.getDataHoraPagamento(),
            cc.getDataHoraEfetivadoPagamento()
        );
    }
}
