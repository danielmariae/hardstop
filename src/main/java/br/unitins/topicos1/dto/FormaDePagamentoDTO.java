package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.FormaDePagamento;
import jakarta.validation.constraints.NotBlank;

public record FormaDePagamentoDTO(
    Long id,
    @NotBlank(message = "O campo nome não pode ser nulo.")
    String nome
) {
    public static FormaDePagamentoDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoDTO(
            fdp.getId(),
            fdp.getNome()
        );
    }
    
}
