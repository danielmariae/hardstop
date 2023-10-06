package br.unitins.topicos1.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class Pedido extends DefaultEntity {
    private String cdr;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
    name = "pedido_statusDoPedido",
    joinColumns = @JoinColumn(name = "id_pedido"),
    inverseJoinColumns = @JoinColumn(name = "id_statusDoPedido")
    )
    private List<StatusDoPedido> sdp;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
    name = "pedido_itemDaVenda",
    joinColumns = @JoinColumn(name = "id_pedido"),
    inverseJoinColumns = @JoinColumn(name = "id_itemDaVenda")
    )
    private List<ItemDaVenda> idv;

    @OneToOne(orphanRemoval = true)
    private Endereco endereco;

    @OneToOne(orphanRemoval = true)
    private FormaDePagamento fdp;

    public FormaDePagamento getFormaDePagamento() {
        return fdp;
    }

    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.fdp = formaDePagamento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<ItemDaVenda> getItemDaVenda() {
        return idv;
    }

    public void setItemDaVenda(List<ItemDaVenda> itemDaVenda) {
        this.idv = itemDaVenda;
    }

    public List<StatusDoPedido> getStatusDoPedido() {
        return sdp;
    }

    public void setStatusDoPedido(List<StatusDoPedido> statusDoPedido) {
        this.sdp = statusDoPedido;
    }

    public String getCodigoDeRastreamento() {
        return cdr;
    }

    public void setCodigoDeRastreamento(String codigoDeRastreamento) {
        this.cdr = codigoDeRastreamento;
    }

}
