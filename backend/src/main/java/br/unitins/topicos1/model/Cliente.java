package br.unitins.topicos1.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Cliente extends DefaultEntity {

  private String nome;
  private LocalDate dataNascimento;
  private String cpf;
  private String sexo;
  private String login;
  private String senha;
  private String email;
  private final Perfil perfil = Perfil.USER;
  private String nomeImagem;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "cliente_endereco",
    joinColumns = @JoinColumn(name = "id_cliente"),
    inverseJoinColumns = @JoinColumn(name = "id_endereco")
  )
  private List<Endereco> listaEndereco;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "cliente_telefone",
    joinColumns = @JoinColumn(name = "id_cliente"),
    inverseJoinColumns = @JoinColumn(name = "id_telefone")
  )
  private List<Telefone> listaTelefone;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(
    name = "lista_de_desejos",
    joinColumns = @JoinColumn(name = "id_cliente"),
    inverseJoinColumns = @JoinColumn(name = "id_produto")
  )
  private List<Produto> listaProduto;
  
  public List<Produto> getListaProduto() {
    return listaProduto;
  }

  public void setListaProduto(List<Produto> listaProduto) {
    this.listaProduto = listaProduto;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getSexo() {
    return sexo;
  }

  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

    public Perfil getPerfil() {
    return perfil;
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

  public String getNomeImagem() {
    return this.nomeImagem;
  }

  public void setNomeImagem(String nomeImagem) {
    this.nomeImagem = nomeImagem;
  }

}
