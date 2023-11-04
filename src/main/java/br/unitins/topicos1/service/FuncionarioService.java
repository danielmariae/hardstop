package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.EnderecoFuncPatchDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;


import java.util.List;


public interface FuncionarioService {

  // Cadastra um funcionário
  public FuncionarioDTO insertFuncionario(FuncionarioDTO dto);

  // Altera todas as informações de funcionário em um dado id
  public FuncionarioResponseDTO updateFuncionario(FuncionarioDTO dto, Long id);

  // Altera ou insere um número de telefone
  public FuncionarioResponseDTO updateTelefoneFuncionario(List<TelefonePatchDTO> tel, Long id);

  // Altera um endereço
  public FuncionarioResponseDTO updateEnderecoFuncionario(EnderecoFuncPatchDTO end, Long id);

    // Altera a senha do funcionario
  public String updateSenhaFuncionario(PatchSenhaDTO senha, Long id);

  // Apaga todos os dados de um funcionário
  public void deleteFuncionario(Long id);

  // Encontra um funcionário pelo id
  public FuncionarioResponseDTO findByIdFuncionario(Long id);

  // Encontra um funcionário pelo cpf
  public FuncionarioResponseDTO findByCpfFuncionario(String cpf);

  // Encontra um funcionário pelo nome
  public List<FuncionarioResponseDTO> findByNameFuncionario(String nome);

  // Retorna uma lista com os dados de todos os funcionários
  public List<FuncionarioResponseDTO> findByAllFuncionario();

  //Autenticação de usuário
  public FuncionarioResponseDTO findByLoginAndSenha(String login, String senha);

  //Autenticação de usuário
  public FuncionarioResponseDTO findByLogin(String login);

}
