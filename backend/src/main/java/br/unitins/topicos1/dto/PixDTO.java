package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Pix;

public record PixDTO(
    String nomeUsuario,
    String nomeRecebedor,
    String chaveRecebedor,
    Double valorPago,
    LocalDateTime dataHoraGeracao,
    LocalDateTime dataHoraEfetivadoPagamento,
    String nomeCidade,
    String nomeArquivo
) {
   public static PixDTO valueOf(Pix pix) {
    return new PixDTO(
        pix.getNomeUsuario(),
        pix.getNomeRecebedor(),
        pix.getChaveRecebedor(),
        pix.getValorPago(),
        pix.getDataHoraGeracao(),
        pix.getDataHoraEfetivadoPagamento(),
        pix.getNomeCidade(),
        pix.getNomeArquivo()
    );
   } 
}
