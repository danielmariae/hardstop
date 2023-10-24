package br.unitins.topicos1.service;


import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioPatchSenhaDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import java.util.List;


public interface FuncionarioService {

  // Cadastra um funcionário
  public FuncionarioDTO insert(FuncionarioDTO dto);

  // Altera todas as informações de funcionário em um dado id
  public FuncionarioResponseDTO update(FuncionarioDTO dto, Long id);

  // Altera ou insere um número de telefone
  public FuncionarioResponseDTO updateTelefone(List<TelefonePatchDTO> tel, Long id);

  // Altera um endereço
  public FuncionarioResponseDTO updateEndereco(EnderecoPatchDTO end, Long id);

  // Altera a senha do funcionário
  public FuncionarioDTO updateSenha(FuncionarioPatchSenhaDTO senha);

  // Apaga todos os dados de um funcionário
  public void delete(Long id);

  // Encontra um funcionário pelo id
  public FuncionarioResponseDTO findById(Long id);

  // Encontra um funcionário pelo cpf
  public FuncionarioResponseDTO findByCpf(String cpf);

  // Encontra um funcionário pelo nome
  public List<FuncionarioResponseDTO> findByName(String nome);

  // Retorna uma lista com os dados de todos os funcionários
  public List<FuncionarioResponseDTO> findByAll();

  //Autenticação de usuário
  public FuncionarioResponseDTO findByLoginAndSenha(String login, String senha);
}
