package br.unitins.topicos1.dto;

import java.util.List;


public record PedidoDTO(
    
    
    FormaDePagamentoDTO formaDePagamento,
    List<ItemDaVendaDTO> itemDaVenda,
    Long idEndereco
) {
    }

