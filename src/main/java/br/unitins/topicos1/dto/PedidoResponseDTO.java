package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Pedido;

public record PedidoResponseDTO(
    Long id,
    String cdr,
    FormaDePagamentoDTO fdp,
    List<ItemDaVendaDTO> idv,
    EnderecoDTO end,
    List<StatusDoPedidoResponseDTO> sdp
) {
    public static PedidoResponseDTO valueOf(Pedido pdd) {
        return new PedidoResponseDTO(
            pdd.getId(),
            pdd.getCodigoDeRastreamento(),
            FormaDePagamentoDTO.valueOf(pdd.getFormaDePagamento()),
            pdd
            .getItemDaVenda()
            .stream()
            .map(i -> ItemDaVendaDTO.valueOf(i))
            .toList(),
            EnderecoDTO.valueOf(pdd.getEndereco()),
            pdd
            .getStatusDoPedido()
            .stream()
            .map(s -> StatusDoPedidoResponseDTO.valueOf(s))
            .toList()
        );
    }
}
