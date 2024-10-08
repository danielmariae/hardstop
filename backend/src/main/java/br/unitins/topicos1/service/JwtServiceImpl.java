package br.unitins.topicos1.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import br.unitins.topicos1.dto.cliente.ClienteResponseDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;
import br.unitins.topicos1.model.utils.Perfil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtServiceImpl implements JwtService {

    private static final Duration EXPIRATION_TIME = Duration.ofHours(24);

    @Override
    public String generateJwt(ClienteResponseDTO dto) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(EXPIRATION_TIME);

        Set<String> roles = new HashSet<String>();
        roles.add("User");

        return Jwt.issuer("hardstop-jwt")
        .subject(dto.login())
        .groups(roles)
        .expiresAt(expiryDate)
        .sign();
    }

    @Override
    public String generateJwt(FuncionarioResponseDTO dto) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(EXPIRATION_TIME);

        Set<String> roles = new HashSet<String>();
        roles.add(Perfil.valueOf(dto.idperfil()).getLabel());

        return Jwt.issuer("hardstop-jwt")
        .subject(dto.login())
        .groups(roles)
        .expiresAt(expiryDate)
        .sign();
    }
}
