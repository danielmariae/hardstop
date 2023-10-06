package br.unitins.topicos1.model;

public enum TipoTelefone {
    
    FIXO_RESIDENCIAL(0, "Telefone fixo residencial"),
    FIXO_COMERCIAL(1, "Telefone fixo comercial"),
    CELULAR_PARTICULAR(2, "Telefone celular particular"),
    CELULAR_COMERCIAL(3, "Telefone celular comercial"),
    TELEFONE_OUTRA_PESSOA(4, "Telefone de outra pessoa");
  
    private Integer id;
    private String descricao;
  
    private TipoTelefone(Integer id, String descricao) {
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
  
    public static TipoTelefone valueOf(Integer id) {
      if (id == null) return null;
  
      for (TipoTelefone status : TipoTelefone.values()) {
        if (status.getId() == id) {
          return status;
        }
      }
      return null;
    }

    @Override
    public String toString() {
      return getDescricao();
    }
  }
  













