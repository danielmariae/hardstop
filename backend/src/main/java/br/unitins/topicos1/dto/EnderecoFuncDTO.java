package br.unitins.topicos1.dto;

import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.model.Endereco;
import jakarta.validation.constraints.NotBlank;

public record EnderecoFuncDTO(

  @NotBlank(message = "O campo não pode ser nulo.")
  String logradouro,
  @NotBlank(message = "O campo não pode ser nulo. Caso não exista, use s/n")
  String numero,
  @NotBlank(message = "O campo não pode ser nulo. Caso inexista, use s/lt")
  String lote,
  @NotBlank(message = "O campo não pode ser nulo.")
  String bairro,
  @NotBlank(message = "O campo não pode ser nulo.")
  String complemento,
  CEP cep,
  @NotBlank(message = "O campo não pode ser nulo.")
  String localidade,
  UnidadeFederativa uf,
  @NotBlank(message = "O campo não pode ser nulo.")
  String pais
) {
  public static EnderecoFuncDTO valueOf(Endereco endereco) {
    return new EnderecoFuncDTO(
      endereco.getLogradouro(),
      endereco.getNumero(),
      endereco.getLote(),
      endereco.getBairro(),
      endereco.getComplemento(),
      endereco.getCep(),
      endereco.getLocalidade(),
      endereco.getUF(),
      endereco.getPais()
    );
  }
}