package br.unitins.topicos1.model;

public class ObjetoRetorno {
    private Boolean boo;
    private String idPedido;
    
    public ObjetoRetorno(Boolean boo, String idPedido) {
        this.boo = boo;
        this.idPedido = idPedido;
    }
    
    public Boolean getBoo() {
        return boo;
    }
    public void setBoo(Boolean boo) {
        this.boo = boo;
    }
    public String getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }
    
   
}
