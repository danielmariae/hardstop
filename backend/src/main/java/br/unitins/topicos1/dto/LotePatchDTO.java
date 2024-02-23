package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotNull;

public record LotePatchDTO(
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Long id,
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Integer quantidade
) {
    
}
