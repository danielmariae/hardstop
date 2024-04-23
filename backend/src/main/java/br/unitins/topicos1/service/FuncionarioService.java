package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.EnderecoFuncDTO;
import br.unitins.topicos1.dto.EnderecoFuncPatchDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioNSDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;


public interface FuncionarioService {

  // Cadastra um funcionário
  public FuncionarioDTO insertFuncionario(FuncionarioDTO dto);

  // Altera todas as informações de funcionário em um dado id
  public FuncionarioResponseDTO updateFuncionario(FuncionarioDTO dto, Long id);

  public FuncionarioResponseDTO updateFuncionarioNS(FuncionarioNSDTO dto, Long id);

  // Substitui algum dado de um telefone
  public FuncionarioResponseDTO updateTelefoneFuncionario(TelefonePatchDTO tel, Long id);

   // Insere um novo telefone
  public FuncionarioResponseDTO insertTelefoneFuncionario(TelefoneDTO tel, Long id);

  // Substitui algum dado de um endereco
  public FuncionarioResponseDTO updateEnderecoFuncionario(EnderecoFuncPatchDTO end, Long id);

  // Insere um novo endereco
  public FuncionarioResponseDTO insertEnderecoFuncionario(EnderecoFuncDTO end, Long id);

  // Altera o nome do funcionario
  public String updateNome(PatchNomeDTO nome, Long id);

  // Altera o cpf do funcionario
  public String updateCpf(PatchCpfDTO cpf, Long id);

  // Altera o login do funcionario
  public String updateLogin(PatchLoginDTO login, Long id);

  // Altera o email do funcionario
  public String updateEmail(PatchEmailDTO email, Long id);

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

    // LISTAR DE FORMA PAGINADA
    public List<FuncionarioResponseDTO> findByAll(int page, int pageSize);

    // CONTADOR:
    public long count();

}
