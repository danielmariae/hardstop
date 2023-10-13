package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Pedido;


public record PedidoDTO(
    
    
    FormaDePagamentoDTO formaDePagamento,
    List<ItemDaVendaDTO> itemDaVenda,
    Long idEndereco
) {
    public static PedidoDTO valueOf(Pedido pdd) {
        return new PedidoDTO(
           
            FormaDePagamentoDTO.valueOf(pdd.getFormaDePagamento()),
            pdd
            .getItemDaVenda()
            .stream()
            .map(i -> ItemDaVendaDTO.valueOf(i))
            .toList(),
            pdd.getIdEndereco()
        );
    }
}
