package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.cliente.ClienteResponseDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;

public interface JwtService {
    public String generateJwt(ClienteResponseDTO dto);
    public String generateJwt(FuncionarioResponseDTO dto);
}
