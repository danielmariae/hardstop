package br.unitins.topicos1.dto;

import java.time.LocalDateTime;
import br.unitins.topicos1.model.StatusDoPedido;
import jakarta.validation.constraints.NotBlank;

public record StatusDoPedidoDTO(
    @NotBlank(message = "O campo nome não pode ser nulo.")
    LocalDateTime dataHora,
    @NotBlank(message = "O campo nome não pode ser nulo.")
    Integer status) 

{
    
    public static StatusDoPedidoDTO valueOf(StatusDoPedido status) {
        return new StatusDoPedidoDTO(
            status.getDataHora(),
            status.getStatus().getId()
        );
    }
}
