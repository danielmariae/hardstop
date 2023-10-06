package br.unitins.topicos1.service;


import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.TelefoneDTO;

import java.util.List;


public interface FuncionarioService {
  public FuncionarioResponseDTO insert(FuncionarioDTO dto);

  public FuncionarioResponseDTO update(FuncionarioDTO dto, Long id);

  public FuncionarioResponseDTO updateTelefone(List<TelefoneDTO> tel, Long id);

  public FuncionarioResponseDTO updateEndereco(EnderecoDTO end, Long id);

  public FuncionarioDTO updateSenha(String senha, Long id);

  public void delete(Long id);

  public FuncionarioResponseDTO findById(Long id);

  public FuncionarioResponseDTO findByCpf(String cpf);

  public List<FuncionarioResponseDTO> findByName(String nome);

  public List<FuncionarioResponseDTO> findByAll();
}
