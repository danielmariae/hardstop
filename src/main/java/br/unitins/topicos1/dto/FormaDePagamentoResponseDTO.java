package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.FormaDePagamento;
import br.unitins.topicos1.model.ModalidadePagamento;


public record FormaDePagamentoResponseDTO(
    Long id,
    ModalidadePagamento modalidade
    
) {
    public static FormaDePagamentoResponseDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoResponseDTO(
            fdp.getId(),
            fdp.getModalidade()
        );
    }
    
}