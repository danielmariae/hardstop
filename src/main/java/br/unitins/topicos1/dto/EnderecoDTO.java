package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(

  @NotBlank(message = "O campo nome não pode ser nulo.")
  String nome,
  @NotBlank(message = "O campo rua não pode ser nulo.")
  String rua,
  @NotBlank(message = "O campo numero não pode ser nulo. Pode inserir s/n.")
  String numero,
  @NotBlank(message = "O campo lote não pode ser nulo. Pode inserir s/l.")
  String lote,
  @NotBlank(message = "O campo bairro não pode ser nulo.")
  String bairro,
  @NotBlank(message = "O campo complemento não pode ser nulo.")
  String complemento,
  @NotBlank(message = "O campo cep não pode ser nulo.")
  @Pattern(regexp = "([0-9]{5}[-./\s][0-9]{3})|([0-9]{8})", message = "Digite um cep válido!")
  String cep,
  @NotBlank(message = "O campo municipio não pode ser nulo.")
  String municipio,
  @NotBlank(message = "O campo Estado não pode ser nulo.")
  String estado,
  @NotBlank(message = "O campo País não pode ser nulo.")
  String pais
) {
  public static EnderecoDTO valueOf(Endereco endereco) {
    return new EnderecoDTO(
      endereco.getNome(),
      endereco.getRua(),
      endereco.getNumero(),
      endereco.getLote(),
      endereco.getBairro(),
      endereco.getComplemento(),
      endereco.getCep(),
      endereco.getMunicipio(),
      endereco.getEstado(),
      endereco.getPais()
    );
  }
}
