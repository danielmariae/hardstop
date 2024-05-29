package br.unitins.topicos1.model.utils;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Telefone extends DefaultEntity {
  
  private String ddd;
  private String numeroTelefone;
 
  @Enumerated(EnumType.ORDINAL)
  private TipoTelefone tipoTelefone;

  public Telefone(String ddd, String numeroTelefone, TipoTelefone tipoTelefone) {
    setDdd(ddd);
    setNumeroTelefone(numeroTelefone);
    setTipoTelefone(tipoTelefone);
  }

  public Telefone() {}

  public void setTipoTelefone(TipoTelefone tipoTelefone) {
    this.tipoTelefone = tipoTelefone;
  }

public TipoTelefone getTipoTelefone() {
  return tipoTelefone;
}

  public String getDdd() {
    return ddd;
  }

  public void setDdd(String ddd) {
    this.ddd = ddd;
  }

  public String getNumeroTelefone() {
    return numeroTelefone;
  }

  public void setNumeroTelefone(String numeroTelefone) {
    this.numeroTelefone = numeroTelefone;
  }

public static Object valueOf(Telefone t) {
    return null;
}
}
