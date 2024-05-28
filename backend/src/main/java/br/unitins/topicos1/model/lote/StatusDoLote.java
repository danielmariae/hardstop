package br.unitins.topicos1.model.lote;


public enum StatusDoLote {
    USADO(0, "Lote já foi finalizado"),
    ATIVO(1, "Lote ativo no momento"),
    ESPERA(2, "Lote cadastrado esperando para ser ativado");

    private Integer id;
    private String descricao;

    private StatusDoLote(Integer id, String descricao) {
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
    
      public static StatusDoLote valueOf(Integer id) {
        if (id == null) return null;
    
        for (StatusDoLote status : StatusDoLote.values()) {
          if (status.getId() == id) {
            return status;
          }
        }
        return null;
      }
    

    
}
