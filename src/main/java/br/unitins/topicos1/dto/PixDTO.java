package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Pix;

public record PixDTO(
    String nomeCliente,
    String nomeRecebedor,
    String chaveRecebedor,
    Double valorPago,
    LocalDateTime dataHoraGeracao,
    LocalDateTime dataHoraEfetivadoPagamento,
    String nomeCidade
) {
   public static PixDTO valueOf(Pix pix) {
    return new PixDTO(
        pix.getNomeCliente(),
        pix.getNomeRecebedor(),
        pix.getChaveRecebedor(),
        pix.getValorPago(),
        pix.getDataHoraGeracao(),
        pix.getDataHoraEfetivadoPagamento(),
        pix.getNomeCidade()
    );
   } 
}
