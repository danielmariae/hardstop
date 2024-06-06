package br.unitins.topicos1.model.pedido;

public enum Status {
  AGUARDANDO_PAGAMENTO(0, "Aguardando pagamento"),
  PAGAMENTO_NÃO_AUTORIZADO(1, "Pagamento não autorizado pela financeira"),
  PAGAMENTO_AUTORIZADO(2, "Pagamento recebido"),
  SEPARADO_DO_ESTOQUE(3, "Separado do estoque"),
  ENTREGUE_A_TRANSPORTADORA(4, "Entregue a transportadora"),
  ENTREGUE(5, "Entregue para o cliente"),
  DESISTIDO(6, "Desistiu da compra antes da separação do estoque"),
  DEVOLVIDO(7, "Devolveu a compra após a entrega");

  private Integer id;
  private String descricao;

  private Status(Integer id, String descricao) {
    setId(id);
    setDescricao(descricao);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public static Status valueOf(Integer id) {
    if (id == null) return null;

    for (Status status : Status.values()) {
      if (status.getId() == id) {
        return status;
      }
    }
    return null;
  }
}
