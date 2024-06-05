package br.unitins.topicos1.dto.pedido.formaPagamento;

import br.unitins.topicos1.model.pagamento.FormaDePagamento;


public record FormaDePagamentoResponseDTO(
    Long id,
    Integer modalidade,
    Double valorPago
    
) {
    public static FormaDePagamentoResponseDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoResponseDTO(
            fdp.getId(),
            fdp.getModalidade().getId(),
            fdp.getValorPago()
        );
    }
    
}