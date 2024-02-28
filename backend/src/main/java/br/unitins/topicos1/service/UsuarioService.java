package br.unitins.topicos1.service;


import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.dto.DesejoResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;


import java.util.List;

public interface UsuarioService {
  // Cadastra um novo cliente
  public UsuarioResponseDTO insertUsuario(UsuarioDTO dto);

  // Substitui todas as informações relacionadas a um cliente com determinado id
  public UsuarioResponseDTO updateUsuario(UsuarioDTO dto, Long id);

  // Substitui algum dado de um telefone
  public UsuarioResponseDTO updateTelefoneUsuario(TelefonePatchDTO tel, Long id);

   // Insere um novo telefone
  public UsuarioResponseDTO insertTelefoneUsuario(TelefoneDTO tel, Long id);

  // Substitui algum dado de um endereco
  public UsuarioResponseDTO updateEnderecoUsuario(EnderecoPatchDTO end, Long id);

  // Insere um novo endereco
  public UsuarioResponseDTO insertEnderecoUsuario(EnderecoDTO end, Long id);

  // Altera o nome do cliente
  public String updateNome(PatchNomeDTO nome, Long id);

  // Altera o cpf do cliente
  public String updateCpf(PatchCpfDTO cpf, Long id);

  // Altera o login do cliente
  public String updateLogin(PatchLoginDTO login, Long id);

  // Altera o email do cliente
  public String updateEmail(PatchEmailDTO email, Long id);

  // Altera a senha do cliente
  public String updateSenhaUsuario(PatchSenhaDTO senha, Long id);

  // Apaga um cliente inteiro
  public void deleteUsuario(Long id);

  // Encontra um cliente usando seu id
  public UsuarioResponseDTO findByIdUsuario(Long id);

  // Encontra um cliente usando seu cpf
  public UsuarioResponseDTO findByCpfUsuario(String cpf);

  // Encontra um cliente usando seu nome
  public List<UsuarioResponseDTO> findByNameUsuario(String nome);

  // Retorna uma lista com os dados de todos os clientes
  public List<UsuarioResponseDTO> findByAllUsuario();

  // Retorna a lista de desejos de um dado usuário
  public List<DesejoResponseDTO> findListaDesejosUsuario(Long id);

  //Autenticação de usuário
  public UsuarioResponseDTO findByLoginAndSenha(String login, String senha);

  //Autenticação de usuário
  public UsuarioResponseDTO findByLogin(String login);

}
