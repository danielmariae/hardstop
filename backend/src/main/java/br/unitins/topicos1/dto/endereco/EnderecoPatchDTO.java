package br.unitins.topicos1.dto.endereco;

import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.model.utils.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoPatchDTO(
  Long id,
  @NotBlank(message = "O campo nome não pode ser nulo.")
  String nome,
  @NotBlank(message = "O campo rua não pode ser nulo.")
  String logradouro,
  @NotBlank(message = "O campo numero não pode ser nulo. Pode inserir s/n.")
  String numeroLote,
  @NotBlank(message = "O campo bairro não pode ser nulo.")
  String bairro,
  @NotBlank(message = "O campo complemento não pode ser nulo.")
  String complemento,
  @NotBlank(message = "O campo não pode ser nulo.")
  @Pattern(regexp = "(\\d{5}-\\d{3})")
  String cep,
  @NotBlank(message = "O campo municipio não pode ser nulo.")
  String localidade,
  UnidadeFederativa uf,
  @NotBlank(message = "O campo País não pode ser nulo.")
  String pais
) {
  public static EnderecoPatchDTO valueOf(Endereco endereco) {
    return new EnderecoPatchDTO(
      endereco.getId(),
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

