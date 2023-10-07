package br.unitins.topicos1.dto;

import java.util.List;

public record FornecedorDTO(
  String nomeFantasia,
  String cnpj,
  String endSite,
  List<EnderecoDTO> listaEndereco,
  List<TelefoneDTO> listaTelefone
) {}
