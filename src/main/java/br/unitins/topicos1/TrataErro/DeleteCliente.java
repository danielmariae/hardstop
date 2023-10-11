package br.unitins.topicos1.TrataErro;

import java.util.List;

import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.StatusDoPedido;

public class DeleteCliente {

    private boolean deletou;
    private String mensagem;
    
    public DeleteCliente(boolean deletou, String mensagem) {
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
    public static boolean podeDeletar(List<Pedido> listaPedidos) {

    // Dá permissão para deletar o cliente que nunca fez um pedido
        if(listaPedidos == null || listaPedidos.isEmpty()) {
            return true;
        }

    // Todos os pedidos feitos pelo cliente já foram finalizados (Status.getId() == 4)?    
        Integer chaveDelecao = 0;
        for(Pedido pedido : listaPedidos) {
            for(StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
                if(statusPedido.getStatus().getId() == 5) {
                    chaveDelecao++;
                }
            }
        }

  // Caso a igualdade seja verdadeira, significa que todos os pedidos do cliente foram finalizados. Deste modo poderemos deletar o cliente do banco de dados junto com todos os seus endereços.
        if(chaveDelecao == listaPedidos.size()) {
            return true;
        } else {
            return false;
        }
    }
}