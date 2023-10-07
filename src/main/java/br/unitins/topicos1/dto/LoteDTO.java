package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;
import jakarta.validation.constraints.NotBlank;

public record LoteDTO(
    @NotBlank(message = "O campo nome não pode ser nulo.")
    String lote,
    Fornecedor fornecedor
) {
    public static LoteDTO valueOf(Lote lote)
    {
        return new LoteDTO(
            lote.getLote(),
            lote.getFornecedor()
        );
    } 
}
