package br.unitins.topicos1.model.produto;

 import java.util.List;

import br.unitins.topicos1.model.lote.Lote;
import br.unitins.topicos1.model.utils.DefaultEntity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Produto extends DefaultEntity {

    private String nome;
    private String modelo;
    private String marca;
    private String descricao;
    private String codigoBarras;
    private Double altura;
    private Double largura;
    private Double comprimento;
    private Double peso;
    private Double valorVenda;
    // QuantidadeUnidades trata de produtos que apresentam quantidades unitárias para venda como por exemplo 1 pacote de arroz ou 10 cartelas de cigarro. O seu uso impede o uso, ao mesmo tempo, dos atributos quantidadeNaoConvencional e unidadeDeMedida.
    private Integer quantidadeUnidades;
    // QuantidadeNaoConvencional e unidadeDeMedida trata de quantidades não convencionais de medição como por exemplo 2.5 Kg ou 1.3 metros. Estes dois atributos sempre andam juntos
    private Double quantidadeNaoConvencional;
    private String unidadeDeMedida;
    private List<String> nomeImagem;
    private String imagemPrincipal;

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
     * @return Integer return the quantidadeUnidades
     */
    public Integer getQuantidadeUnidades() {
        return quantidadeUnidades;
    }

    /**
     * @param quantidadeUnidades the quantidadeUnidades to set
     */
    public void setQuantidadeUnidades(Integer quantidadeUnidades) {
        this.quantidadeUnidades = quantidadeUnidades;
    }

    /**
     * @return Double return the quantidadeNaoConvencional
     */
    public Double getQuantidadeNaoConvencional() {
        return quantidadeNaoConvencional;
    }

    /**
     * @param quantidadeNaoConvencional the quantidadeNaoConvencional to set
     */
    public void setQuantidadeNaoConvencional(Double quantidadeNaoConvencional) {
        this.quantidadeNaoConvencional = quantidadeNaoConvencional;
    }


    /**
     * @return String return the unidadeDeMedida
     */
    public String getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    /**
     * @param unidadeDeMedida the unidadeDeMedida to set
     */
    public void setUnidadeDeMedida(String unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }

    public String getImagemPrincipal() {
        return imagemPrincipal;
    }

    public void setImagemPrincipal(String imagemPrincipal) {
        this.imagemPrincipal = imagemPrincipal;
    }

}
