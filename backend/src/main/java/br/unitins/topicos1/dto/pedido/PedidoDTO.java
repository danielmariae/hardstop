package br.unitins.topicos1.dto.pedido;

import java.util.List;

import br.unitins.topicos1.dto.pedido.formaPagamento.FormaDePagamentoDTO;
import br.unitins.topicos1.dto.pedido.itemVenda.ItemDaVendaDTO;
import jakarta.validation.constraints.NotNull;


public record PedidoDTO(
    
    @NotNull(message = "O campo Forma de Pagamento não pode ser nulo")
    FormaDePagamentoDTO formaDePagamento,
    @NotNull(message = "O campo Item da Venda não pode ser nulo")
    List<ItemDaVendaDTO> itemDaVenda,
    @NotNull(message = "O campo idEndereco não pode ser nulo")
    Long idEndereco,
    @NotNull(message = "O campo idCliente não pode ser nulo")
    Long idCliente
) {
    }

