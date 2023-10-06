package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.TelefoneDTO;

import java.util.List;

public interface ClienteService {
  public ClienteResponseDTO insert(ClienteDTO dto);

  public ClienteResponseDTO update(ClienteDTO dto, Long id);

  public ClienteResponseDTO updateTelefone(List<TelefoneDTO> tel, Long id);

  public ClienteResponseDTO updateEndereco(List<EnderecoDTO> end, Long id);

  public ClienteDTO updateSenha(String senha, Long id);

  public void delete(Long id);

  public ClienteResponseDTO findById(Long id);

  public ClienteResponseDTO findByCpf(String cpf);

  public List<ClienteResponseDTO> findByName(String nome);

  public List<ClienteResponseDTO> findByAll();
}
