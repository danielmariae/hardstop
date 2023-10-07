package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Pedido;
import jakarta.validation.constraints.NotBlank;

public record PedidoDTO(
    Long id,
    String codigoDeRastreamento,
    @NotBlank(message = "O campo Forma de Pagamento não pode ser nulo.")
    FormaDePagamentoDTO formaDePagamento,
    @NotBlank(message = "O campo ItemDaVenda não pode ser nulo.")
    List<ItemDaVendaDTO> itemDaVenda,
    @NotBlank(message = "O campo endereco não pode ser nulo.")
    EnderecoDTO endereco,
    List<StatusDoPedidoDTO> statusDoPedido
) {
    public static PedidoDTO valueOf(Pedido pdd) {
        return new PedidoDTO(
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
            .map(s -> StatusDoPedidoDTO.valueOf(s))
            .toList()
        );
    }
}
