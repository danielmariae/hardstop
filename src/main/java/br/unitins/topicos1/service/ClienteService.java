package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClientePatchSenhaDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.PedidoResponseDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import java.util.List;

public interface ClienteService {
  // Cadastra um novo cliente
  public ClienteResponseDTO insert(ClienteDTO dto);

  // Substitui todas as informações relacionadas a um cliente com determinado id
  public ClienteResponseDTO update(ClienteDTO dto, Long id);

  // Substitui um telefone ou insere um novo telefone
  public ClienteResponseDTO updateTelefone(List<TelefoneDTO> tel, Long id);

  // Substitui um endereco ou insere um novo endereco
  public ClienteResponseDTO updateEndereco(List<EnderecoDTO> end, Long id);

  // Altera a senha do cliente
  public ClienteDTO updateSenha(ClientePatchSenhaDTO senha);

  // Apaga um cliente inteiro
  public void delete(Long id);

  // Encontra um cliente usando seu id
  public ClienteResponseDTO findById(Long id);

  // Encontra um cliente usando seu cpf
  public ClienteResponseDTO findByCpf(String cpf);

  // Encontra um cliente usando seu nome
  public List<ClienteResponseDTO> findByName(String nome);

  // Retorna uma lista com os dados de todos os clientes
  public List<ClienteResponseDTO> findByAll();

  // Altera o status de um pedido
  public ClienteResponseDTO updateStatusDoPedido(
    PedidoPatchStatusDTO ppsdto,
    Long idcliente
  );

  // Retorna todos os pedidos relacionados a um cliente específico
  public List<PedidoResponseDTO> findPedidoByCliente(Long idcliente);
}
