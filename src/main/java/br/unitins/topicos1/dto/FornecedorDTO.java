package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Lote;
import br.unitins.topicos1.model.Telefone;

public record FornecedorDTO(
  String nomeFantasia,
  String nomeRegistro,
  String endSite,
  List<Endereco> listaEndereco,
  List<Telefone> listaTelefone,
  List<Lote> listaLote
) {}
