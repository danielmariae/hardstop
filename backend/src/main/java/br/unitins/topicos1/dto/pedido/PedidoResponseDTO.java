package br.unitins.topicos1.dto.pedido;

import java.util.List;

import br.unitins.topicos1.dto.endereco.EnderecoResponseDTO;
import br.unitins.topicos1.dto.pedido.formaPagamento.FormaDePagamentoResponseDTO;
import br.unitins.topicos1.dto.pedido.itemVenda.ItemDaVendaResponseDTO;
import br.unitins.topicos1.dto.pedido.status.StatusDoPedidoResponseDTO;
import br.unitins.topicos1.model.pedido.Pedido;

public record PedidoResponseDTO(
    Long id,
    String codigoDeRastreamento,
    FormaDePagamentoResponseDTO formaDePagamento,
    List<ItemDaVendaResponseDTO> itemDaVenda,
    EnderecoResponseDTO endereco,
    List<StatusDoPedidoResponseDTO> statusDoPedido
) {
    public static PedidoResponseDTO valueOf(Pedido pdd) {
        return new PedidoResponseDTO(
            pdd.getId(),
            pdd.getCodigoDeRastreamento(),
            FormaDePagamentoResponseDTO.valueOf(pdd.getFormaDePagamento()),
            pdd
            .getItemDaVenda()
            .stream()
            .map(i -> ItemDaVendaResponseDTO.valueOf(i))
            .toList(),
            EnderecoResponseDTO.valueOf(pdd.getEndereco()),
            pdd
            .getStatusDoPedido()
            .stream()
            .map(s -> StatusDoPedidoResponseDTO.valueOf(s))
            .toList()
        );
    }
}
