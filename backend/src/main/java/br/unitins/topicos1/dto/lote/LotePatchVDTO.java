package br.unitins.topicos1.dto.lote;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LotePatchVDTO(
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Long id,
    @NotBlank(message = "O campo lote não pode ser nulo.")
    String lote,
    @NotNull(message = "O campo Custo da compra não pode ser nulo")
    @Digits(integer = 6, fraction = 3, message = "Por favor, digite um número válido")
    Double valorVenda
) {
    
}
