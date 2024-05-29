package br.unitins.topicos1.model.pedido;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

import br.unitins.topicos1.model.utils.DefaultEntity;

@Entity
public class StatusDoPedido extends DefaultEntity {

  private LocalDateTime dataHora;

  @Enumerated(EnumType.ORDINAL)
  private Status status;

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public void setStatus(Status status) {
    this.status = status;
}

public Status getStatus() {
    return status;
  }
}
