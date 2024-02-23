package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Telefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TelefoneDTO(

  Integer tipo,
  @NotBlank(message = "O campo ddd não pode ser nulo.")
  @Pattern(regexp = "([0-9]{2,3})", message = "Digite um ddd válido!")
  String ddd,
  @NotBlank(message = "O campo numero não pode ser nulo.")
  @Pattern(regexp = "([0-9]{5}[-./\s][0-9]{4})|([0-9]{9})", message = "Digite um número de telefone válido!")
  String numeroTelefone) {
  public static TelefoneDTO valueOf(Telefone telefone) {
    return new TelefoneDTO(
      telefone.getTipoTelefone().getId(),
      telefone.getDdd(),
      telefone.getNumeroTelefone()
    );
  }
}
