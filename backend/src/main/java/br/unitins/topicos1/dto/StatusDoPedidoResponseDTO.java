package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Status;
import br.unitins.topicos1.model.StatusDoPedido;

public record StatusDoPedidoResponseDTO(
    Long id,
    LocalDateTime dataHora,
    Status status) 

{
    
    public static StatusDoPedidoResponseDTO valueOf(StatusDoPedido status) {
        return new StatusDoPedidoResponseDTO(
            status.getId(),
            status.getDataHora(),
            status.getStatus()
        );
    }
}
