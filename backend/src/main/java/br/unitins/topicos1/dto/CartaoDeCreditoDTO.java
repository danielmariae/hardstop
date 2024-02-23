package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.CartaoDeCredito;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record CartaoDeCreditoDTO(
    String numeroCartao,
    @NotNull(message = "O campo não pode ser nulo")
    @Digits(integer = 2, fraction = 0, message = "Por favor, digite um número válido")
    Integer mesValidade,
    @NotNull(message = "O campo não pode ser nulo")
    @Digits(integer = 2, fraction = 0, message = "Por favor, digite um número válido")
    Integer anoValidade,
    @NotNull(message = "O campo não pode ser nulo")
    @Digits(integer = 3, fraction = 0, message = "Por favor, digite um número válido")
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
