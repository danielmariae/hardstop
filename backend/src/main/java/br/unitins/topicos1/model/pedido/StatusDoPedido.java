package br.unitins.topicos1.model.pedido;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

  @ManyToOne
  @JoinColumn(name = "id_pedido", nullable = false, referencedColumnName = "id")
    private Pedido pedido;

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public void setStatus(Status status) {
    this.status = status;
}

public Status getStatus() {
    return status;
  }

    /**
     * @return Pedido return the pedido
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * @param pedido the pedido to set
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

}
