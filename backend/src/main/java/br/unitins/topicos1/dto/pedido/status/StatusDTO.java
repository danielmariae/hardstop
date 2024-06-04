package br.unitins.topicos1.dto.pedido.status;

import br.unitins.topicos1.model.pedido.Status;

public record StatusDTO(
    Integer id,
    String descricao
) {
    public static StatusDTO valueOf(Status status){
        return new StatusDTO(status.getId(), status.getDescricao());
    }
}
