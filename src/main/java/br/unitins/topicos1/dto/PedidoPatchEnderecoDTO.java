package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotNull;

public record PedidoPatchEnderecoDTO(

    @NotNull(message = "O campo Id do Pedido não pode ser nulo")
    Long idPedido,
    @NotNull(message = "O campo Id do Endereco não pode ser nulo")
    Long idEndereco
)
    
{}
