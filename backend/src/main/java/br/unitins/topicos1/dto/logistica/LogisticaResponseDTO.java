package br.unitins.topicos1.dto.logistica;


import br.unitins.topicos1.dto.endereco.EnderecoResponseDTO;
import br.unitins.topicos1.dto.telefone.TelefoneResponseDTO;
import br.unitins.topicos1.model.utils.Logistica;

import java.util.List;

public record LogisticaResponseDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone
) {
  public static LogisticaResponseDTO valueOf(Logistica logistica) {
    return new LogisticaResponseDTO(
      logistica.getNomeFantasia(),
      logistica.getCnpj(),
      logistica.getEndSite(),
      logistica
        .getListaEndereco()
        .stream()
        .map(e -> EnderecoResponseDTO.valueOf(e))
        .toList(),
      logistica
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneResponseDTO.valueOf(t))
        .toList()
    );
  }
}
