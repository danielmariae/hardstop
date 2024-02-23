package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Boleto;

public record BoletoDTO(
    String nomeBanco,
    LocalDateTime dataHoraGeracao,
    LocalDateTime dataHoraLimitePag,
    LocalDateTime dataHoraEfetivadoPagamento,
    Double valorPago,
    String nomeArquivo
) {
    public static BoletoDTO valueOf(Boleto bol) {
        return new BoletoDTO(
        bol.getNomeBanco(),
        bol.getDataHoraGeracao(),
        bol.getDataHoraLimitePag(),
        bol.getDataHoraEfetivadoPagamento(),
        bol.getValorPago(),
        bol.getNomeArquivo()
        );
    }
}
