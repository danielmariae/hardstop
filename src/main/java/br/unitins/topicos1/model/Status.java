package br.unitins.topicos1.model;

public enum Status {
  AGUARDANDO_PAGAMENTO(0, "Aguardando confirmação do pagamento"),
  PAGO(1, "Pagamento recebido"),
  SEPARADO_DO_ESTOQUE(2, "Separado do estoque"),
  ENTREGUE_A_TRANSPORTADORA(3, "Entregue para a transportadora"),
  ENTREGUE(4, "Entregue");

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
