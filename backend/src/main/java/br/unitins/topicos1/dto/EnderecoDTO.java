package br.unitins.topicos1.dto;

import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.model.Endereco;
import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(

  @NotBlank(message = "O campo não pode ser nulo.")
  String nome,
  @NotBlank(message = "O campo não pode ser nulo.")
  String logradouro,
  @NotBlank(message = "O campo não pode ser nulo. Caso não exista, use s/n")
  String numeroLote,
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
  public static EnderecoDTO valueOf(Endereco endereco) {
    return new EnderecoDTO(
      endereco.getNome(),
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
