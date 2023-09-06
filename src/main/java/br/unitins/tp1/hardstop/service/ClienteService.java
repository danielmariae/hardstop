package br.unitins.tp1.hardstop.service;

import java.util.List;

import br.unitins.tp1.hardstop.dto.ClienteDTO;
import br.unitins.tp1.hardstop.dto.ClienteResponseDTO;
import br.unitins.tp1.hardstop.model.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.PathParam;

@ApplicationScoped
public interface ClienteService {
    public ClienteResponseDTO insert(ClienteDTO dto);
    public ClienteResponseDTO update(ClienteDTO dto, Long id);
    public void delete(Long id);
    public Cliente findById(Long id);
    public List<ClienteResponseDTO> findByNome(@PathParam("nome")String nome);
    public List<ClienteResponseDTO> findAll();
}
