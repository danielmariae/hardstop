package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Telefone;
import java.util.List;

public record FornecedorResponseDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<Endereco> listaEndereco,
  List<Telefone> listaTelefone
) {

public static FornecedorResponseDTO valueOf(Fornecedor fornecedor) {
    return new FornecedorResponseDTO(
      fornecedor.getNomeFantasia(),
      fornecedor.getNomeRegistro(),
      fornecedor.getEndSite(),
      fornecedor
        .getListaEndereco()
        .stream()
        .map(e -> Endereco.valueOf(e))
        .toList(),
      fornecedor
        .getListaTelefone()
        .stream()
        .map(t -> Telefone.valueOf(t))
        .toList()
    );
  }
}
