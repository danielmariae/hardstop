package br.unitins.topicos1.dto.telefone;

import br.unitins.topicos1.model.utils.Telefone;

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
