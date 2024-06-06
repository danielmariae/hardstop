package br.unitins.topicos1.dto.pedido;

import jakarta.validation.constraints.NotNull;

public record PedidoPatchStatusDTO(

    String codigoDeRastreamento,
    Integer idTransportadora,
    @NotNull(message = "O campo Id do Pedido não pode ser nulo")
    Long idPedido,
    @NotNull(message = "O campo Id do status não pode ser nulo")
    Integer idStatus
    
) {
    
}
