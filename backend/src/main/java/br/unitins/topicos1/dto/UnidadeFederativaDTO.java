package br.unitins.topicos1.dto;

import org.jrimum.domkee.pessoa.UnidadeFederativa;

public record UnidadeFederativaDTO(
    String nome,
    String sigla,
    String capital
) {
    public static UnidadeFederativaDTO valueOf(UnidadeFederativa uf) {
        return new UnidadeFederativaDTO(
           uf.getNome(),
           uf.getSigla(),
           uf.getCapital()
        );
    }
}
