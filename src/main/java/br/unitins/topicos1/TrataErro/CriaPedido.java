package br.unitins.topicos1.TrataErro;

import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Endereco;

public class CriaPedido {

    private boolean criou;
    private String mensagem;

    public CriaPedido(boolean criou, String mensagem) {
        setCriou(criou);
        setMensagem(mensagem);
    }
    public boolean isCriou() {
        return criou;
    }
    public void setCriou(boolean criou) {
        this.criou = criou;
    }
    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public static CriaPedido verificaEnderecoCliente(Cliente cliente, Endereco endereco) {
        for (Endereco end : cliente.getListaEndereco()) {
            // O pedido repassado ao método pertence ao Cliente repassado ao método.
            if (end.getId() == endereco.getId()) {
                return new CriaPedido(true, "O id de endereço fornecido pertence ao cliente em questão.");
            }
        }
        return new CriaPedido(false, "O id de endereço fornecido não pertence ao cliente em questão! Operação não realizada!");
    }

    public static CriaPedido temEmEstoque(Integer num_produtos_banco, Integer num_produtos_pedido) {
        Integer sub;
        sub = num_produtos_banco - num_produtos_pedido;
        // Existem produtos em quantidade adequada no banco
        if(sub >= 0){
           
            return new CriaPedido(true, "Existem produtos em quantidade suficiente no banco de dados");
        // Não existem produtos em quantidade adequada no banco
        } else {
           
            return new CriaPedido(false, "Não existem produtos em quantidade suficiente no banco de dados! Operação não realizada!");
        }
    }
}
