package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Funcionario;
import java.time.LocalDate;
import java.util.List;

public record FuncionarioResponseDTO(
  Long id,
  String nome,
  LocalDate dataNascimento,
  String cpf,
  String sexo,
  String login,
  String email,
  EnderecoResponseDTO endereco,
  List<TelefoneResponseDTO> listaTelefone
) {
  public static FuncionarioResponseDTO valueOf(Funcionario funcionario) {
    return new FuncionarioResponseDTO(
      funcionario.getId(),
      funcionario.getNome(),
      funcionario.getDataNascimento(),
      funcionario.getCpf(),
      funcionario.getSexo(),
      funcionario.getLogin(),
      funcionario.getEmail(),
      EnderecoResponseDTO.valueOf(funcionario.getEndereco()),
      funcionario
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneResponseDTO.valueOf(t))
        .toList()
    );
  }
}
