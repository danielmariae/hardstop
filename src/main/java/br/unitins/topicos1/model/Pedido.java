package br.unitins.topicos1.model;

import java.util.List;

import br.unitins.topicos1.TrataErro.CriaPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Pedido extends DefaultEntity {
    private String codigoDeRastreamento;
    private Long idEndereco;
    private CriaPedido trataErroPedido;


    public CriaPedido getTrataErroPedido() {
        return trataErroPedido;
    }

    public void setTrataErroPedido(CriaPedido trataErroPedido) {
        this.trataErroPedido = trataErroPedido;
    }

    public Long getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Long idEndereco) {
        this.idEndereco = idEndereco;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
    name = "pedido_statusDoPedido",
    joinColumns = @JoinColumn(name = "id_pedido"),
    inverseJoinColumns = @JoinColumn(name = "id_statusDoPedido")
    )
    private List<StatusDoPedido> statusDoPedido;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
    name = "pedido_itemDaVenda",
    joinColumns = @JoinColumn(name = "id_pedido"),
    inverseJoinColumns = @JoinColumn(name = "id_itemDaVenda")
    )
    private List<ItemDaVenda> itemDaVenda;

    // Não posso usar CascadeType.REMOVE porque endereço é uma entidade compartilhada com outras várias outras entidades como Cliente, Funcionário, Fornecedor e Logística
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_formaDepagamento")
    private FormaDePagamento formaDePagamento;

    public FormaDePagamento getFormaDePagamento() {
        return formaDePagamento;
    }

    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<ItemDaVenda> getItemDaVenda() {
        return itemDaVenda;
    }

    public void setItemDaVenda(List<ItemDaVenda> itemDaVenda) {
        this.itemDaVenda = itemDaVenda;
    }

    public List<StatusDoPedido> getStatusDoPedido() {
        return statusDoPedido;
    }

    public void setStatusDoPedido(List<StatusDoPedido> statusDoPedido) {
        this.statusDoPedido = statusDoPedido;
    }

    public String getCodigoDeRastreamento() {
        return codigoDeRastreamento;
    }

    public void setCodigoDeRastreamento(String codigoDeRastreamento) {
        this.codigoDeRastreamento = codigoDeRastreamento;
    }

}
