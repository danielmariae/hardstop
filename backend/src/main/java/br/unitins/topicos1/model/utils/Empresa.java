package br.unitins.topicos1.model.utils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Empresa extends DefaultEntity {

  private String nomeReal;
  private String nomeFantasia;
  private String nomeResponsavel;
  private String cnpj;
  private String cpf;
  private String chavePixAleatoria;
  private String chavePixTelefone;
  private String chavePixCnpj;
  private String chavePixCpf;
  private String email;
  private String nomeBanco;
  private String codigoBanco;
  private String numeroConta;
  private String numeroAgencia;
  private Boolean isEmpresa;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "empresa_endereco",
    joinColumns = @JoinColumn(name = "id_empresa"),
    inverseJoinColumns = @JoinColumn(name = "id_endereco")
  )
  private List<Endereco> listaEndereco;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "empresa_telefone",
    joinColumns = @JoinColumn(name = "id_empresa"),
    inverseJoinColumns = @JoinColumn(name = "id_telefone")
  )
  private List<Telefone> listaTelefone;

public String getNomeReal() {
    return nomeReal;
}

public void setNomeReal(String nomeReal) {
    this.nomeReal = nomeReal;
}

public String getNomeFantasia() {
    return nomeFantasia;
}

public void setNomeFantasia(String nomeFantasia) {
    this.nomeFantasia = nomeFantasia;
}

public String getNomeResponsavel() {
    return nomeResponsavel;
}

public void setNomeResponsavel(String nomeResponsavel) {
    this.nomeResponsavel = nomeResponsavel;
}

public String getCnpj() {
    return cnpj;
}

public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
}

public String getCpf() {
    return cpf;
}

public void setCpf(String cpf) {
    this.cpf = cpf;
}

public String getChavePixAleatoria() {
    return chavePixAleatoria;
}

public void setChavePixAleatoria(String chavePixAleatoria) {
    this.chavePixAleatoria = chavePixAleatoria;
}

public String getChavePixTelefone() {
    return chavePixTelefone;
}

public void setChavePixTelefone(String chavePixTelefone) {
    this.chavePixTelefone = chavePixTelefone;
}

public String getChavePixCnpj() {
    return chavePixCnpj;
}

public void setChavePixCnpj(String chavePixCnpj) {
    this.chavePixCnpj = chavePixCnpj;
}

public String getChavePixCpf() {
    return chavePixCpf;
}

public void setChavePixCpf(String chavePixCpf) {
    this.chavePixCpf = chavePixCpf;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getNomeBanco() {
    return nomeBanco;
}

public void setNomeBanco(String nomeBanco) {
    this.nomeBanco = nomeBanco;
}

public String getCodigoBanco() {
    return codigoBanco;
}

public void setCodigoBanco(String codigoBanco) {
    this.codigoBanco = codigoBanco;
}

public String getNumeroConta() {
    return numeroConta;
}

public void setNumeroConta(String numeroConta) {
    this.numeroConta = numeroConta;
}

public String getNumeroAgencia() {
    return numeroAgencia;
}

public void setNumeroAgencia(String numeroAgencia) {
    this.numeroAgencia = numeroAgencia;
}

public Boolean getIsEmpresa() {
    return isEmpresa;
}

public void setIsEmpresa(Boolean isEmpresa) {
    this.isEmpresa = isEmpresa;
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

  
}
