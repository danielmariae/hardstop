package br.unitins.topicos1.service;


import java.util.List;

import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClienteNSDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.PerfilDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;

public interface ClienteService {
  // Cadastra um novo cliente
  public ClienteResponseDTO insertCliente(ClienteDTO dto);

  // Substitui todas as informações relacionadas a um cliente com determinado id
  public ClienteResponseDTO updateCliente(ClienteDTO dto, Long id);

  // Substitui todas as informações relacionadas a um cliente com determinado id (exceto Senha)
  public ClienteResponseDTO updateClienteNS(ClienteNSDTO dto, Long id);

  // Substitui algum dado de um telefone
  public ClienteResponseDTO updateTelefoneCliente(TelefonePatchDTO tel, Long id);

   // Insere um novo telefone
  public ClienteResponseDTO insertTelefoneCliente(TelefoneDTO tel, Long id);

  // Substitui algum dado de um endereco
  public ClienteResponseDTO updateEnderecoCliente(EnderecoPatchDTO end, Long id);

  // Insere um novo endereco
  public ClienteResponseDTO insertEnderecoCliente(EnderecoDTO end, Long id);

  // Altera o nome do cliente
  public String updateNome(PatchNomeDTO nome, Long id);

  // Altera o cpf do cliente
  public String updateCpf(PatchCpfDTO cpf, Long id);

  // Altera o login do cliente
  public String updateLogin(PatchLoginDTO login, Long id);

  // Altera o email do cliente
  public String updateEmail(PatchEmailDTO email, Long id);

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

    // LISTAR DE FORMA PAGINADA
    public List<ClienteResponseDTO> findByAll(int page, int pageSize);

    // CONTADOR:
    public long count();

    public PerfilDTO findPerfilByLogin(String login);
  
}
