package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.FormaDePagamento;


public record FormaDePagamentoResponseDTO(
    Long id,
    String modalidade
    
) {
    public static FormaDePagamentoResponseDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoResponseDTO(
            fdp.getId(),
            fdp.getModalidade()
        );
    }
    
}