package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotNull;

public record PedidoPatchStatusDTO(

    @NotNull(message = "O campo Id do Pedido não pode ser nulo")
    Long idPedido,
    @NotNull(message = "O campo Id do status não pode ser nulo")
    Integer idStatus,
    String codigoDeRastreamento
) {
    
}
