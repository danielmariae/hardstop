package br.unitins.topicos1.TrataErro;

import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.StatusDoPedido;

public class DeletePedido {

  private boolean deletou;
  private String mensagem;

  public DeletePedido(boolean deletou, String mensagem) {
    setDeletou(deletou);
    setMensagem(mensagem);
  }

  public boolean isDeletou() {
    return deletou;
  }

  public void setDeletou(boolean deletou) {
    this.deletou = deletou;
  }

  public String getMensagem() {
    return mensagem;
  }

  public void setMensagem(String mensagem) {
    this.mensagem = mensagem;
  }

  public static DeletePedido podeDeletar(Cliente cliente, Pedido pedido) {
    for (Pedido pedidoteste : cliente.getListaPedido()) {
      // O pedido repassado ao método pertence ao Cliente repassado ao método.
      if (pedidoteste.getId() == pedido.getId()) {
        // Coletando o Status de número mais elevado neste pedido
        Integer chaveDelecao = 0;
        for (StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
          if (chaveDelecao < statusPedido.getStatus().getId()) {
            chaveDelecao = statusPedido.getStatus().getId();
          }
        }

        // Pedidos com status do tipo: AGUARDANDO_PAGAMENTO ou PAGAMENTO_NÃO_AUTORIZADO podem ser deletados
        if (chaveDelecao == 0) {
          return new DeletePedido(
            true,
            "Pedido AGUARDANDO_PAGAMENTO foi excluído com sucesso!"
          );
        } else if (chaveDelecao == 1) {
          return new DeletePedido(
            true,
            "Pedido PAGAMENTO_NÃO_AUTORIZADO foi excluído com sucesso!"
          );
          // Pedidos com status do tipo: PAGO, SEPARADO_DO_ESTOQUE, ENTREGUE_A_TRANSPORTADORA e ENTREGUE não podem ser deletados
        } else if (chaveDelecao == 2) {
          return new DeletePedido(false, "Pedido PAGO não pode ser excluído!");
        } else if (chaveDelecao == 3) {
          return new DeletePedido(
            false,
            "Pedido SEPARADO_DO_ESTOQUE não pode ser excluído!"
          );
        } else if (chaveDelecao == 4) {
          return new DeletePedido(
            false,
            "Pedido ENTREGUE_A_TRANSPORTADORA não pode ser excluído!"
          );
        } else if (chaveDelecao == 5) {
          return new DeletePedido(
            false,
            "Pedido ENTREGUE não pode ser excluído!"
          );
        }
        // Dá permissão para deletar o pedido vazio
        return new DeletePedido(
          false,
          "O Status constante neste pedido não existe no Enum Status!"
        );
      } 
    }
    // O pedido repassado ao método não pertence ao Cliente repassado ao método.
    return new DeletePedido(false,"Este pedido não pertence a este Cliente e por isso não pôde ser excluído!");
  }
}
