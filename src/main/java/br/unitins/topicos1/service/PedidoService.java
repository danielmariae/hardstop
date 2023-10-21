package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.TrataErro.CriaPedido;
import br.unitins.topicos1.TrataErro.DeletePedido;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.PedidoResponseDTO;
import jakarta.ws.rs.PathParam;

public interface PedidoService {

// Cria um novo pedido atrelado a um dado cliente
public PedidoResponseDTO insert(PedidoDTO dto, @PathParam("id") Long id);

// Apaga todos os pedidos de um determinado cliente
public DeletePedido deletePedidoByCliente(Long idCliente, Long idPedido);

    // Altera o status de um pedido
  public PedidoResponseDTO updateStatusDoPedido(PedidoPatchStatusDTO ppsdto);

  // Altera o endereço de um pedido
  public CriaPedido updateEndereco(PedidoPatchEnderecoDTO dto, Long id);

    // Retorna todos os pedidos relacionados a um cliente específico
  public List<PedidoResponseDTO> findPedidoByCliente(Long idcliente);
}
