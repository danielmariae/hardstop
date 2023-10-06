package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Endereco;

public record EnderecoResponseDTO(
  Long id,
  String nome,
  String rua,
  String numero,
  String lote,
  String bairro,
  String complemento,
  String cep,
  String municipio,
  String estado,
  String pais
) {
  public static EnderecoResponseDTO valueOf(Endereco endereco) {
    return new EnderecoResponseDTO(
      endereco.getId(),
      endereco.getNome(),
      endereco.getRua(),
      endereco.getNumero(),
      endereco.getLote(),
      endereco.getBairro(),
      endereco.getComplemento(),
      endereco.getCep(),
      endereco.getMunicipio(),
      endereco.getEstado(),
      endereco.getPais()
    );
  }
}
