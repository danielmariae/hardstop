package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.ItemDaVendaDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.PedidoResponseDTO;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.Status;
import br.unitins.topicos1.model.StatusDoPedido;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PathParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

  @Inject
  ClienteRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Override
  public PedidoResponseDTO insert(PedidoDTO dto, @PathParam("id") Long id) {
    Cliente cliente = repository.findById(id);
    Pedido pedido = new Pedido();

    pedido.setCodigoDeRastreamento(dto.codigoDeRastreamento());
    pedido.getFormaDePagamento().setNome(dto.formaDePagamento().nome());
    pedido.getEndereco().setNome(dto.endereco().nome());
    pedido.getEndereco().setRua(dto.endereco().rua());
    pedido.getEndereco().setNumero(dto.endereco().numero());
    pedido.getEndereco().setLote(dto.endereco().lote());
    pedido.getEndereco().setBairro(dto.endereco().bairro());
    pedido.getEndereco().setComplemento(dto.endereco().complemento());
    pedido.getEndereco().setCep(dto.endereco().cep());
    pedido.getEndereco().setMunicipio(dto.endereco().municipio());
    pedido.getEndereco().setEstado(dto.endereco().estado());
    pedido.getEndereco().setPais(dto.endereco().pais());

    pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
    for (ItemDaVendaDTO idv : dto.itemDaVenda()) {
      ItemDaVenda item = new ItemDaVenda();
      item.setPreco(idv.preco());
      item.setQuantidade(idv.quantidade());
      pedido.getItemDaVenda().add(item);
    }

    pedido.setStatusDoPedido(new ArrayList<StatusDoPedido>());
    StatusDoPedido status = new StatusDoPedido();
    status.setDataHora(LocalDateTime.now());
    status.setStatus(Status.valueOf(0));
    pedido.getStatusDoPedido().add(status);

    cliente.getListaPedido().add(pedido);
    return PedidoResponseDTO.valueOf(pedido);
  }

  @Override
  @Transactional
  public void deletePedidoByCliente(Long idCliente) {
    Cliente cliente = repository.findById(idCliente);
    for (Pedido pedido : cliente.getListaPedido()) {
      for (StatusDoPedido status : pedido.getStatusDoPedido()) {
        repositoryPedido.deleteById(status.getId());
      }
      for (ItemDaVenda item : pedido.getItemDaVenda()) {
        repositoryPedido.deleteById(item.getId());
      }

      repositoryPedido.deleteById(pedido.getFormaDePagamento().getId());

      repositoryPedido.deleteById(pedido.getEndereco().getId());

      repositoryPedido.deleteById(pedido.getId());
    }
  }

  @Override
  public PedidoResponseDTO updateStatusDoPedido(PedidoPatchStatusDTO ppsdto) {
    Pedido pedido = repositoryPedido.findById(ppsdto.idPedido());

    StatusDoPedido statusVenda = new StatusDoPedido();
    statusVenda.setDataHora(LocalDateTime.now());
    statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
    pedido.getStatusDoPedido().add(statusVenda);
    repositoryPedido.persist(pedido);
    return PedidoResponseDTO.valueOf(pedido);
  }

  @Override
  public List<PedidoResponseDTO> findPedidoByCliente(Long idcliente) {
    Cliente cliente = repository.findById(idcliente);
    return cliente
      .getListaPedido()
      .stream()
      .map(p -> PedidoResponseDTO.valueOf(p))
      .toList();
  }
}
