package br.unitins.topicos1.model;

 import java.util.List;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import jakarta.persistence.Entity;

@Entity
public class Produto extends DefaultEntity {

    private String nome;
    private String modelo;
    private String cpu;
    private String chipset;
    private String memoria;
    private String bios;
    private String grafico;
    private String lan;
    private String slots;
    private String armazenamento;
    private String marca;
    private String descricao;
    private String codigoBarras;
    private Double altura;
    private Double largura;
    private Double comprimento;
    private Double peso;
    private Double valorVenda;
    private Integer quantidade;
    private List<String> nomeImagem;

    @OneToOne
    private Lote loteAtual;

    @ManyToOne
    private Classificacao classificacao;

    @Version
    private int version;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getLargura() {
        return largura;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public void setComprimento(Double comprimento) {
        this.comprimento = comprimento;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public List<String> getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(List<String> nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }
   

    public Lote getLoteAtual() {
        return this.loteAtual;
    }

    public void setLoteAtual(Lote loteAtual) {
        this.loteAtual = loteAtual;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    /**
     * @return String return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    /**
     * @return String return the modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return String return the cpu
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * @return String return the chipset
     */
    public String getChipset() {
        return chipset;
    }

    /**
     * @param chipset the chipset to set
     */
    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    /**
     * @return String return the memoria
     */
    public String getMemoria() {
        return memoria;
    }

    /**
     * @param memoria the memoria to set
     */
    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    /**
     * @return String return the bios
     */
    public String getBios() {
        return bios;
    }

    /**
     * @param bios the bios to set
     */
    public void setBios(String bios) {
        this.bios = bios;
    }

    /**
     * @return String return the grafico
     */
    public String getGrafico() {
        return grafico;
    }

    /**
     * @param grafico the grafico to set
     */
    public void setGrafico(String grafico) {
        this.grafico = grafico;
    }

    /**
     * @return String return the lan
     */
    public String getLan() {
        return lan;
    }

    /**
     * @param lan the lan to set
     */
    public void setLan(String lan) {
        this.lan = lan;
    }

    /**
     * @return String return the slots
     */
    public String getSlots() {
        return slots;
    }

    /**
     * @param slots the slots to set
     */
    public void setSlots(String slots) {
        this.slots = slots;
    }

    /**
     * @return String return the armazenamento
     */
    public String getArmazenamento() {
        return armazenamento;
    }

    /**
     * @param armazenamento the armazenamento to set
     */
    public void setArmazenamento(String armazenamento) {
        this.armazenamento = armazenamento;
    }

}
