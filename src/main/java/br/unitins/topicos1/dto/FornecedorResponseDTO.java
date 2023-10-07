package br.unitins.topicos1.dto;

<<<<<<< HEAD
import java.util.List;

import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;
import br.unitins.topicos1.model.Telefone;
=======

import br.unitins.topicos1.model.Fornecedor;
import java.util.List;
>>>>>>> b45ca0047e85f676e58560a5e7e8e451b6439ac0

public record FornecedorResponseDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
<<<<<<< HEAD
  List<Endereco> listaEndereco,
  List<Telefone> listaTelefone, 
  List<Lote> listaLote
  ) {
=======
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone
) {
>>>>>>> b45ca0047e85f676e58560a5e7e8e451b6439ac0

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
<<<<<<< HEAD
        .map(t -> Telefone.valueOf(t))
        .toList(),
      fornecedor
        .getListaLote()
        .stream()
        .map(l -> Lote.valueOf(l))
        .toList());
=======
        .map(t -> TelefoneResponseDTO.valueOf(t))
        .toList()
    );
>>>>>>> b45ca0047e85f676e58560a5e7e8e451b6439ac0
  }
}
