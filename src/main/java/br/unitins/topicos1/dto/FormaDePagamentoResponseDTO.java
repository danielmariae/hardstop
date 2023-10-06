package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.FormaDePagamento;
import jakarta.validation.constraints.NotBlank;

public record FormaDePagamentoResponseDTO(
    Long id,
    @NotBlank(message = "O campo nome não pode ser nulo.")
    String nome
) {
    public static FormaDePagamentoResponseDTO valueOf(FormaDePagamento fdp) {
        return new FormaDePagamentoResponseDTO(
            fdp.getId(),
            fdp.getNome()
        );
    }
    
}