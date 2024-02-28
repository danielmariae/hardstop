package br.unitins.topicos1.model;

import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.UnidadeFederativa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Endereco extends DefaultEntity {

  
  private String nome;
  private String logradouro;
  private String numeroLote;
  private String bairro;
  private String complemento;
  @Column(name = "cep")
  private CEP cep;
  private String localidade;
  @Enumerated(EnumType.STRING)
  private UnidadeFederativa uf;
  private String pais;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getNumeroLote() {
    return numeroLote;
  }

  public void setNumeroLote(String numeroLote) {
    this.numeroLote = numeroLote;
  }


  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public CEP getCep() {
    return cep;
  }

  public void setCep(CEP cep) {
    this.cep = cep;
  }

  public String getLocalidade() {
    return localidade;
  }

  public void setLocalidade(String localidade) {
    this.localidade = localidade;
  }

  public UnidadeFederativa getUF() {
    return uf;
  }

  public void setUF(UnidadeFederativa uf) {
    this.uf = uf;
  }

  public String getPais() {
    return pais;
  }

  public void setPais(String pais) {
    this.pais = pais;
  }

  public Endereco() {}

  
}
