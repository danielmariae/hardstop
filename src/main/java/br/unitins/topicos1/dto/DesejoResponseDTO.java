package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Produto;

public record DesejoResponseDTO(
    String nome,
    String descricao
) {
    public static DesejoResponseDTO valueOf(Produto produto) {
        return new DesejoResponseDTO(
            produto.getNome(),
            produto.getDescricao()
        );
    }
}
