package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.cliente.DesejoResponseDTO;
import br.unitins.topicos1.dto.endereco.EnderecoDTO;
import br.unitins.topicos1.dto.endereco.EnderecoResponseDTO;
import br.unitins.topicos1.dto.pedido.PedidoDTO;
import br.unitins.topicos1.dto.pedido.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.pedido.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.pedido.PedidoResponseDTO;
import br.unitins.topicos1.dto.pedido.formaPagamento.CartaoDeCreditoResponseDTO;
import br.unitins.topicos1.dto.pedido.status.StatusDoPedidoResponseDTO;


public interface PedidoService {

// Insere um pedido desatrelado de um usuário no banco de dados
public EnderecoResponseDTO insertEndereco(EnderecoDTO dto);

// Cria um novo pedido atrelado a um dado cliente
public PedidoResponseDTO insert(PedidoDTO dto);

// Apaga todos os pedidos de um determinado cliente
public void deletePedidoByCliente(Long idCliente, Long idPedido);

    // Altera o status de um pedido
  public PedidoResponseDTO updateStatusDoPedido(PedidoPatchStatusDTO ppsdto);

  // Altera o endereço de um pedido
  public EnderecoResponseDTO updateEndereco(PedidoPatchEnderecoDTO dto, Long id);

    // Retorna todos os pedidos relacionados a um cliente específico
  public List<PedidoResponseDTO> findPedidoByCliente(Long idCliente);

  // Retorna um pedido pelo id.
  public PedidoResponseDTO findPedidoById(Long id);

  // Insere produtos na lista de desejos do Cliente
  public DesejoResponseDTO insertDesejos(Long idProduto, Long idCliente);

  // Remove produtos na lista de desejos do Cliente
  public void deleteDesejos(Long idProduto, Long idCliente);

  // Procurar cartão relacionado ao pedido
  public CartaoDeCreditoResponseDTO findCartaoByPedido(Long id);

  // Procurar boleto relacionado ao pedido
  // public BoletoResponseDTO findBoletoByPedido(Long id);

  // Procurar stauts do pedido
  public StatusDoPedidoResponseDTO findByStatus(Long id);
  
}
