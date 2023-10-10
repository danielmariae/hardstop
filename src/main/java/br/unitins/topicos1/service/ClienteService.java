package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClientePatchSenhaDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.ClienteResponseNPDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
// import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;

import java.util.List;

public interface ClienteService {
  // Cadastra um novo cliente
  public ClienteResponseNPDTO insert(ClienteDTO dto);

  // Substitui todas as informações relacionadas a um cliente com determinado id
  public ClienteResponseDTO update(ClienteDTO dto, Long id);

  // Substitui um telefone ou insere um novo telefone
  public ClienteResponseDTO updateTelefone(List<TelefonePatchDTO> tel, Long id);

  // Substitui um endereco ou insere um novo endereco
  public ClienteResponseDTO updateEndereco(List<EnderecoPatchDTO> end, Long id);

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

  // Retorna a lista de desejos de um dado usuário
  public List<ProdutoResponseDTO> findListaDesejos(Long id);

}
