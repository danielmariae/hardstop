package br.unitins.topicos1.dto.pedido.formaPagamento;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.pagamento.Pix;

public record PixResponseDTO(
    String nomeCliente,
    String nomeRecebedor,
    String chaveRecebedor,
    Double valorPago,
    LocalDateTime dataHoraGeracao,
    LocalDateTime dataHoraEfetivadoPagamento,
    String nomeCidade,
    String nomeArquivo
) {
   public static PixResponseDTO valueOf(Pix pix) {
    return new PixResponseDTO(
        pix.getNomeCliente(),
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
