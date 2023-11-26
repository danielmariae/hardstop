package br.unitins.topicos1.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Logistica extends DefaultEntity{

  private String nomeFantasia;
  private String cnpj;
  private String endSite;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "logistica_endereco",
    joinColumns = @JoinColumn(name = "id_logistica"),
    inverseJoinColumns = @JoinColumn(name = "id_endereco")
  )
  private List<Endereco> listaEndereco;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "logistica_telefone",
    joinColumns = @JoinColumn(name = "id_logistica"),
    inverseJoinColumns = @JoinColumn(name = "id_telefone")
  )
  private List<Telefone> listaTelefone;

  public String getNomeFantasia() {
    return nomeFantasia;
  }

  public void setNomeFantasia(String nomeFantasia) {
    this.nomeFantasia = nomeFantasia;
  }

  public String getEndSite() {
    return endSite;
  }

  public void setEndSite(String endSite) {
    this.endSite = endSite;
  }

  public List<Endereco> getListaEndereco() {
    return listaEndereco;
  }

  public void setListaEndereco(List<Endereco> listaEndereco) {
    this.listaEndereco = listaEndereco;
  }

  public List<Telefone> getListaTelefone() {
    return listaTelefone;
  }

  public void setListaTelefone(List<Telefone> listaTelefone) {
    this.listaTelefone = listaTelefone;
  }

  public String getCnpj() {
    return this.cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

}
