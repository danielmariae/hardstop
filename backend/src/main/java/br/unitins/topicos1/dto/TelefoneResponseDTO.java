package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Telefone;

public record TelefoneResponseDTO(
  Long id,
  Integer tipo,
  String ddd,
  String numeroTelefone
) {
  public static TelefoneResponseDTO valueOf(Telefone telefone) {
    return new TelefoneResponseDTO(
      telefone.getId(),
      telefone.getTipoTelefone().getId(),
      telefone.getDdd(),
      telefone.getNumeroTelefone()
    );
  }
}
