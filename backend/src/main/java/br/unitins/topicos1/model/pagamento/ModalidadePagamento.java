package br.unitins.topicos1.model.pagamento;

public enum ModalidadePagamento {
  CARTAO_DE_CREDITO(0, "Cartão de Crédito"),
  BOLETO_BANCARIO(1, "Boleto Bancário"),
  PIX(2, "Pix");
 

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

  public static ModalidadePagamento valueOf(Integer id) {
    if (id == null) return null;

    for (ModalidadePagamento modalidade : ModalidadePagamento.values()) {
      if (modalidade.getId() == id) {
        return modalidade;
      }
    }
    return null;
  }
}
