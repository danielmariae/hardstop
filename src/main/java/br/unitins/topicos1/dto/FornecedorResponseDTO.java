package br.unitins.topicos1.dto;

import java.util.List;
import br.unitins.topicos1.model.Fornecedor;


public record FornecedorResponseDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone, 
  List<LoteResponseDTO> listaLote) {
  public static FornecedorResponseDTO valueOf(Fornecedor fornecedor) {
      return new FornecedorResponseDTO(
        fornecedor.getNomeFantasia(),
        fornecedor.getNomeRegistro(),
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
          .toList(),
        fornecedor
          .getListaLote()
          .stream()
          .map(l -> LoteResponseDTO.valueOf(l))
          .toList()
        );
  }
}
