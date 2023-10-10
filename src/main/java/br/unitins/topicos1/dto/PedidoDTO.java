package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Pedido;
import jakarta.validation.constraints.NotBlank;

public record PedidoDTO(
    
    //@NotBlank(message = "O campo nome não pode ser nulo.")
    //String codigoDeRastreamento,
    FormaDePagamentoDTO formaDePagamento,
    List<ItemDaVendaDTO> itemDaVenda,
    EnderecoDTO endereco
    /* List<StatusDoPedidoDTO> statusDoPedido */
) {
    public static PedidoDTO valueOf(Pedido pdd) {
        return new PedidoDTO(
            //pdd.getCodigoDeRastreamento(),
            FormaDePagamentoDTO.valueOf(pdd.getFormaDePagamento()),
            pdd
            .getItemDaVenda()
            .stream()
            .map(i -> ItemDaVendaDTO.valueOf(i))
            .toList(),
            EnderecoDTO.valueOf(pdd.getEndereco())
            /* pdd
            .getStatusDoPedido()
            .stream()
            .map(s -> StatusDoPedidoDTO.valueOf(s))
            .toList() */
        );
    }
}
