package br.unitins.topicos1.dto;

import java.util.List;
import br.unitins.topicos1.model.Fornecedor;

public record FornecedorResponseDTO(
  String nomeFantasia,
  String cnpj,
  String endSite,
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone 
  ) {

  public static FornecedorResponseDTO valueOf(Fornecedor fornecedor) {
      return new FornecedorResponseDTO(
        fornecedor.getNomeFantasia(),
        fornecedor.getCnpj(),
        fornecedor.getEndSite(),
        fornecedor
          .getListaEndereco()
          .stream()
          .map(e -> EnderecoResponseDTO.valueOf(e))
          .toList(),
        fornecedor
          .getListaTelefone()
          .stream()
          .map(t -> TelefoneResponseDTO.valueOf(t))
          .toList()
             );
  }
}
