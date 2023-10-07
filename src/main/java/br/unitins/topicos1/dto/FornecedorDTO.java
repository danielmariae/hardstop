package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Fornecedor;

public record FornecedorDTO(
  String nomeFantasia,
  String cnpj,
  String endSite,
  List<EnderecoDTO> listaEndereco,
  List<TelefoneDTO> listaTelefone
) {
  public static FornecedorDTO valueOf(Fornecedor fornecedor) {
    return new FornecedorDTO(
    fornecedor.getNomeFantasia(),
    fornecedor.getCnpj(),
    fornecedor.getEndSite(),
    fornecedor
        .getListaEndereco()
        .stream()
        .map(e -> EnderecoDTO.valueOf(e))
        .toList(),
      fornecedor
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneDTO.valueOf(t))
        .toList()
    );
  }
}
