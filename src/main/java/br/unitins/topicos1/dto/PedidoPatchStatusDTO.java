package br.unitins.topicos1.dto;

public record PedidoPatchStatusDTO(
    Long idPedido,
    Integer idStatus,
    String codigoDeRastreamento
) {
    
}
