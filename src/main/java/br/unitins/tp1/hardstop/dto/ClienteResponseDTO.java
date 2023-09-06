package br.unitins.tp1.hardstop.dto;

import br.unitins.tp1.hardstop.model.Cliente;

public record ClienteResponseDTO(
    Long id,
    String nome,
    String cpf
){
    public static ClienteResponseDTO valueOf(Cliente cliente){
        return new ClienteResponseDTO(
            cliente.getId(), 
            cliente.getNome(), 
            cliente.getCpf());       
    }
}