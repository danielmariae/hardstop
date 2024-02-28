package br.unitins.topicos1.dto;

import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.model.Endereco;

public record EnderecoResponseDTO(
  Long id,
  String nome,
  String logradouro,
  String numeroLote,
  String bairro,
  String complemento,
  CEP cep,
  String localidade,
  UnidadeFederativa uf,
  String pais
) {
  public static EnderecoResponseDTO valueOf(Endereco endereco) {
    return new EnderecoResponseDTO(
      endereco.getId(),
      endereco.getNome(),
      endereco.getLogradouro(),
      endereco.getNumeroLote(),
      endereco.getBairro(),
      endereco.getComplemento(),
      endereco.getCep(),
      endereco.getLocalidade(),
      endereco.getUF(),
      endereco.getPais()
    );
  }
}
