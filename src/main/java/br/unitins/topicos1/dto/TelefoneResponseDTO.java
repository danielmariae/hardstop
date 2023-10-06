package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;

public record TelefoneResponseDTO(
  Long id,
  TipoTelefone tipo,
  String ddd,
  String numeroTelefone
) {
  public static TelefoneResponseDTO valueOf(Telefone telefone) {
    return new TelefoneResponseDTO(
      telefone.getId(),
      telefone.getTipoTelefone(),
      telefone.getDdd(),
      telefone.getNumeroTelefone()
    );
  }
}
