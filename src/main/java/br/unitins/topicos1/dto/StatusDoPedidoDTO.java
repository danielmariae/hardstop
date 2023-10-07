package br.unitins.topicos1.dto;

import java.time.LocalDateTime;
import br.unitins.topicos1.model.StatusDoPedido;

public record StatusDoPedidoDTO(
   
    LocalDateTime dataHora,
    
    Integer status) 

{
    
    public static StatusDoPedidoDTO valueOf(StatusDoPedido status) {
        return new StatusDoPedidoDTO(
            status.getDataHora(),
            status.getStatus().getId()
        );
    }
}
