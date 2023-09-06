package br.unitins.tp1.hardstop.dto;

public class ClienteDTO {
    private final String nome;
    private final String cpf;

    public ClienteDTO(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }
}
