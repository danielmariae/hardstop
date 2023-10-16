package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.FormaDePagamento;
import br.unitins.topicos1.model.ModalidadePagamento;
import jakarta.validation.constraints.NotBlank;

public record FormaDePagamentoDTO(
    
   Integer modalidade
) {
    public static FormaDePagamentoDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoDTO(
            fdp.getModalidade().getId()
        );
    }
}
