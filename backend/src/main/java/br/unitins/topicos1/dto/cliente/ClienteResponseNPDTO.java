package br.unitins.topicos1.dto.cliente;

import java.time.LocalDate;
import java.util.List;

import br.unitins.topicos1.dto.endereco.EnderecoResponseDTO;
import br.unitins.topicos1.dto.telefone.TelefoneResponseDTO;
import br.unitins.topicos1.model.utils.Cliente;

public record ClienteResponseNPDTO(
  Long id,
  String nome,
  LocalDate dataNascimento,
  String cpf,
  String sexo,
  String login,
  String email,
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone
) {
  public static ClienteResponseNPDTO valueOf(Cliente cliente) {
    return new ClienteResponseNPDTO(
      cliente.getId(),
      cliente.getNome(),
      cliente.getDataNascimento(),
      cliente.getCpf(),
      cliente.getSexo(),
      cliente.getLogin(),
      cliente.getEmail(),
      cliente
        .getListaEndereco()
        .stream()
        .map(e -> EnderecoResponseDTO.valueOf(e))
        .toList(),
      cliente
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneResponseDTO.valueOf(t))
        .toList()
    );
  }
}
