package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Lote;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteDTO(
    @NotBlank(message = "O campo lote não pode ser nulo.")
    String lote,
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    FornecedorDTO fornecedor
) {
    public static LoteDTO valueOf(Lote lote)
    {
        return new LoteDTO(
            lote.getLote(),
            FornecedorDTO.valueOf(lote.getFornecedor())
        );
    } 
}
