package br.unitins.topicos1.service;


import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.ClienteResponseNPDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;


import java.util.List;

public interface ClienteService {
  // Cadastra um novo cliente
  public ClienteResponseNPDTO insertCliente(ClienteDTO dto);

  // Substitui todas as informações relacionadas a um cliente com determinado id
  public ClienteResponseDTO updateCliente(ClienteDTO dto, Long id);

  // Substitui um telefone ou insere um novo telefone
  public ClienteResponseDTO updateTelefoneCliente(List<TelefonePatchDTO> tel, Long id);

  // Substitui um endereco ou insere um novo endereco
  public ClienteResponseDTO updateEnderecoCliente(List<EnderecoPatchDTO> end, Long id);

  // Altera a senha do cliente
  public String updateSenhaCliente(PatchSenhaDTO senha, Long id);

  // Apaga um cliente inteiro
  public void deleteCliente(Long id);

  // Encontra um cliente usando seu id
  public ClienteResponseDTO findByIdCliente(Long id);

  // Encontra um cliente usando seu cpf
  public ClienteResponseDTO findByCpfCliente(String cpf);

  // Encontra um cliente usando seu nome
  public List<ClienteResponseDTO> findByNameCliente(String nome);

  // Retorna uma lista com os dados de todos os clientes
  public List<ClienteResponseDTO> findByAllCliente();

  // Retorna a lista de desejos de um dado usuário
  public List<ProdutoResponseDTO> findListaDesejosCliente(Long id);

  //Autenticação de usuário
  public ClienteResponseDTO findByLoginAndSenha(String login, String senha);

  //Autenticação de usuário
  public ClienteResponseDTO findByLogin(String login);

  public ClienteResponseDTO updateNomeImagem(Long id, String nomeImagem);

}
