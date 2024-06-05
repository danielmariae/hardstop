package br.unitins.topicos1.dto.pedido.formaPagamento;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.pagamento.CartaoDeCredito;

public record CartaoDeCreditoResponseDTO(
    Long id,
    String numeroCartao,
    Double valorPago,
    LocalDateTime dataHoraPagamento,
    LocalDateTime dataHoraEfetivadoPagamento
) {
    public static CartaoDeCreditoResponseDTO valueOf(CartaoDeCredito cc) {
        return new CartaoDeCreditoResponseDTO(
            cc.getId(),
            cc.getNumeroCartaoFront(),
            cc.getValorPago(),
            cc.getDataHoraPagamento(),
            cc.getDataHoraEfetivadoPagamento()
        );
    }
}
