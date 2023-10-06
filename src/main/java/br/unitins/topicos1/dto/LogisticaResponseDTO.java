package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Logistica;
import br.unitins.topicos1.model.Telefone;
import java.util.List;

public record LogisticaResponseDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<Endereco> listaEndereco,
  List<Telefone> listaTelefone
) {
  public static LogisticaResponseDTO valueOf(Logistica logistica) {
    return new LogisticaResponseDTO(
      logistica.getNomeFantasia(),
      logistica.getNomeRegistro(),
      logistica.getEndSite(),
      logistica
        .getListaEndereco()
        .stream()
        .map(e -> Endereco.valueOf(e))
        .toList(),
      logistica
        .getListaTelefone()
        .stream()
        .map(t -> Telefone.valueOf(t))
        .toList()
    );
  }
}
