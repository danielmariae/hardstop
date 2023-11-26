package br.unitins.topicos1.dto;

import java.util.List;

public record LogisticaDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<EnderecoDTO> listaEndereco,
  List<TelefoneDTO> listaTelefone
) {}
