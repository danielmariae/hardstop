package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Funcionario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record FuncionarioDTO(

  @NotBlank(message = "O campo nome não pode ser nulo.")
  String nome,
  @Pattern(regexp = "([0-9]{4}[-/.\s][0-9]{2}[-/.\s][0-9]{2})|([0-9]{2}[/.\s][0-9]{2}[/.\s][0-9]{4})", message = "Informe uma data válida!")
  String dataNascimento,
  @NotBlank(message = "O campo cpf não pode ser nulo.")
  @Pattern(regexp = "([0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{2})|([0-9]{11})", message = "Valor digitado inválido!")
  String cpf,
  String sexo,
  @NotBlank(message = "O campo login não pode ser nulo.")
  String login,
  @NotBlank(message = "O campo senha não pode ser nulo.")
  @Pattern(regexp = "(?=^.{6,10}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{:;'?/<.>,])(?!.*\\s).*$", message = "Senha forte (pelo menos 1 letra maiúscula, 1 letra minúscula, 1 dígito, 1 caractere especial). Entre 6 a 10 caracteres." )
  String senha,
  @NotBlank(message = "O campo email não pode ser nulo.")
  @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Digite um email válido!")
  String email,
  EnderecoFuncDTO endereco,
  Integer idperfil,
  List<TelefoneDTO> listaTelefone
) {

public static FuncionarioDTO valueOf(Funcionario funcionario) {
    return new FuncionarioDTO(
      funcionario.getNome(),
      funcionario.getDataNascimento().toString(),
      funcionario.getCpf(),
      funcionario.getSexo(),
      funcionario.getLogin(),
      funcionario.getSenha(),
      funcionario.getEmail(),
      EnderecoFuncDTO.valueOf(funcionario.getEndereco()),
      funcionario.getPerfil().getId(),
      funcionario
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneDTO.valueOf(t))
        .toList()
    );
  }


}
