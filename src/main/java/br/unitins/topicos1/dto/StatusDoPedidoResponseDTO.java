package br.unitins.topicos1.dto;

import java.time.LocalDateTime;

import br.unitins.topicos1.model.Status;
import br.unitins.topicos1.model.StatusDoPedido;
import jakarta.validation.constraints.NotBlank;

public record StatusDoPedidoResponseDTO(
    @NotBlank(message = "O campo nome não pode ser nulo.")
    LocalDateTime dataHora,
    @NotBlank(message = "O campo nome não pode ser nulo.")
    Status status) 

{
    
    public static StatusDoPedidoResponseDTO valueOf(StatusDoPedido status) {
        return new StatusDoPedidoResponseDTO(
            status.getDataHora(),
            status.getStatus()
        );
    }
}
