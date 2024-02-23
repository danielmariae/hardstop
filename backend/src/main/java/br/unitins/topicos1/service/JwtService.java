package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;

public interface JwtService {
    public String generateJwt(ClienteResponseDTO dto);
    public String generateJwt(FuncionarioResponseDTO dto);
}
