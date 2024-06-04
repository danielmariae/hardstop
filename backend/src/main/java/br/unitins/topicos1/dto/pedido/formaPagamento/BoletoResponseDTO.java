package br.unitins.topicos1.dto.pedido.formaPagamento;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.pagamento.Boleto;

public record BoletoResponseDTO(
    String nomeBanco,
    LocalDateTime dataHoraGeracao,
    LocalDateTime dataHoraLimitePag,
    LocalDateTime dataHoraEfetivadoPagamento,
    Double valorPago,
    String nomeArquivo
) {
    public static BoletoResponseDTO valueOf(Boleto bol) {
        return new BoletoResponseDTO(
        bol.getNomeBanco(),
        bol.getDataHoraGeracao(),
        bol.getDataHoraLimitePag(),
        bol.getDataHoraEfetivadoPagamento(),
        bol.getValorPago(),
        bol.getNomeArquivo()
        );
    }
}
