package br.unitins.topicos1.dto.pedido.status;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.pedido.StatusDoPedido;
import jakarta.validation.constraints.NotNull;

public record StatusDoPedidoDTO(
   
    @NotNull(message = "O campo Data e Hora não pode ser nulo")
    LocalDateTime dataHora,
    @NotNull(message = "O campo Status não pode ser nulo")
    Integer status
    ) 

{
    
    public static StatusDoPedidoDTO valueOf(StatusDoPedido status) {
        return new StatusDoPedidoDTO(
            status.getDataHora(),
            status.getStatus().getId()
        );
    }
}
