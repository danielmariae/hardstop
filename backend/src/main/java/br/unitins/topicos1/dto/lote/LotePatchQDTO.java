package br.unitins.topicos1.dto.lote;

import jakarta.validation.constraints.NotNull;

public record LotePatchQDTO(
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Long id,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida
) {
    
}
