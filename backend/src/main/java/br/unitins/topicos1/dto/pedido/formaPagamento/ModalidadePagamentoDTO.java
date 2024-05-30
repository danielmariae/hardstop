package br.unitins.topicos1.dto.pedido.formaPagamento;

import br.unitins.topicos1.model.pagamento.ModalidadePagamento;

public record ModalidadePagamentoDTO(
    Integer id,
    String descricao
) {
    public static ModalidadePagamentoDTO valueOf(ModalidadePagamento mp) {
        return new ModalidadePagamentoDTO(
            mp.getId(),
            mp.getDescricao()
            );
       } 
}