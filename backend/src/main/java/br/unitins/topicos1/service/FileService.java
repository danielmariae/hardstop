package br.unitins.topicos1.service;

import java.io.IOException;

import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import jakarta.ws.rs.core.StreamingOutput;

public interface FileService {
    
    String salvarU(String nomeArquivo, byte[] arquivo) throws IOException;

    String salvarP(String nomeArquivo, byte[] arquivo) throws IOException;

    StreamingOutput obterU(String nomeArquivo);

    StreamingOutput obterP(String nomeArquivo);

    FuncionarioResponseDTO updateNomeImagemF(Long id, String nomeImagem);

    UsuarioResponseDTO updateNomeImagemC(Long id, String nomeImagem);

    ProdutoResponseDTO updateNomeImagemP(Long id, String nomeImagem);

}
