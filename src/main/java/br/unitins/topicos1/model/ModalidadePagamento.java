package br.unitins.topicos1.model;

public enum ModalidadePagamento {
  CARTÃO_DE_CRÉDITO(0, "Aguardando confirmação do pagamento"),
  BOLETO_BANCÁRIO(1, "Pagamento não autorizado pela financeira"),
  PIX(2, "Pagamento recebido");
 

  private Integer id;
  private String descricao;

  private ModalidadePagamento(Integer id, String descricao) {
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
