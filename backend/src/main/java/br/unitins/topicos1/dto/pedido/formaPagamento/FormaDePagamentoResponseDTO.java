package br.unitins.topicos1.dto.pedido.formaPagamento;

import br.unitins.topicos1.model.pagamento.FormaDePagamento;
import br.unitins.topicos1.model.pagamento.ModalidadePagamento;


public record FormaDePagamentoResponseDTO(
    Long id,
    ModalidadePagamento modalidade,
    Double valorPago
    
) {
    public static FormaDePagamentoResponseDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoResponseDTO(
            fdp.getId(),
            fdp.getModalidade(),
            fdp.getValorPago()
        );
    }
    
}