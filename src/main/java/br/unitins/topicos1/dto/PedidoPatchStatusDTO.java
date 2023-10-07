package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;

public record PedidoPatchStatusDTO(
    Long idPedido,
    @NotBlank(message = "O campo status não pode ser nulo.")
    Integer idStatus
) {
    
}
