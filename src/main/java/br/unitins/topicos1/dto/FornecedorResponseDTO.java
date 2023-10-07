package br.unitins.topicos1.dto;


import br.unitins.topicos1.model.Fornecedor;
import java.util.List;

public record FornecedorResponseDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone
) {

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
        .toList()
    );
  }
}
