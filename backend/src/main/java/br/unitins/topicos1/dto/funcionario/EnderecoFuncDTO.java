package br.unitins.topicos1.dto.funcionario;

import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.model.utils.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoFuncDTO(

  @NotBlank(message = "O campo não pode ser nulo.")
  String logradouro,
  @NotBlank(message = "O campo não pode ser nulo. Caso não exista, use s/n")
  String numeroLote,
  @NotBlank(message = "O campo não pode ser nulo.")
  String bairro,
  @NotBlank(message = "O campo não pode ser nulo.")
  String complemento,
  @NotBlank(message = "O campo não pode ser nulo.")
  @Pattern(regexp = "(\\d{5}-\\d{3})")
  String cep,
  @NotBlank(message = "O campo não pode ser nulo.")
  String localidade,
  UnidadeFederativa uf,
  @NotBlank(message = "O campo não pode ser nulo.")
  String pais
) {
  public static EnderecoFuncDTO valueOf(Endereco endereco) {
    return new EnderecoFuncDTO(
      endereco.getLogradouro(),
      endereco.getNumeroLote(),
      endereco.getBairro(),
      endereco.getComplemento(),
      endereco.getCep(),
      endereco.getLocalidade(),
      endereco.getUF(),
      endereco.getPais()
    );
  }
}