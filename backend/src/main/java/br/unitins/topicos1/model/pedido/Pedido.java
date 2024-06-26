package br.unitins.topicos1.model.pedido;

import java.util.List;

import br.unitins.topicos1.model.pagamento.FormaDePagamento;
import br.unitins.topicos1.model.utils.Cliente;
import br.unitins.topicos1.model.utils.DefaultEntity;
import br.unitins.topicos1.model.utils.Endereco;
import br.unitins.topicos1.model.utils.Logistica;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Pedido extends DefaultEntity {
    private String codigoDeRastreamento;
    // private Long idEndereco;

    // public Long getIdEndereco() {
    //     return idEndereco;
    // }

    // public void setIdEndereco(Long idEndereco) {
    //     this.idEndereco = idEndereco;
    // }

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinTable(
    //name = "pedido_statusDoPedido",
    //joinColumns = @JoinColumn(name = "id_pedido"),
    //inverseJoinColumns = @JoinColumn(name = "id_statusDoPedido")
    //)
    private List<StatusDoPedido> statusDoPedido;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(
    name = "pedido_itemDaVenda",
    joinColumns = @JoinColumn(name = "id_pedido"),
    inverseJoinColumns = @JoinColumn(name = "id_itemDaVenda")
    )
    private List<ItemDaVenda> itemDaVenda;

    // Não posso usar CascadeType.REMOVE porque endereço é uma entidade compartilhada com várias outras entidades como Cliente, Funcionário, Fornecedor e Logística
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_formaDepagamento")
    private FormaDePagamento formaDePagamento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_logistica")
    private Logistica logistica;

    public Pedido() {}

    public Pedido(Pedido pedido) {
        this.codigoDeRastreamento = pedido.codigoDeRastreamento;
        this.cliente = pedido.cliente;
        this.statusDoPedido = pedido.statusDoPedido;
        this.itemDaVenda = pedido.itemDaVenda;
        this.endereco = pedido.endereco;
        this.formaDePagamento = pedido.formaDePagamento;
        this.logistica = pedido.logistica;
    }


    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Logistica getLogistica() {
        return logistica;
    }

    public void setLogistica(Logistica logistica) {
        this.logistica = logistica;
    }

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
