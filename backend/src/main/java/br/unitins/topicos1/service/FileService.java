package br.unitins.topicos1.service;

import java.io.IOException;

import br.unitins.topicos1.dto.cliente.ClienteResponseDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.produto.ProdutoResponseDTO;
import jakarta.ws.rs.core.StreamingOutput;

public interface FileService {
    
    String salvarU(String nomeArquivo, byte[] arquivo) throws IOException;

    String salvarP(String nomeArquivo, byte[] arquivo) throws IOException;

    StreamingOutput obterU(String nomeArquivo);

    StreamingOutput obterP(String nomeArquivo);

    FuncionarioResponseDTO updateNomeImagemF(Long id, String nomeImagem);

    ClienteResponseDTO updateNomeImagemC(Long id, String nomeImagem);

    ProdutoResponseDTO updateNomeImagemP(Long id, String nomeImagem);

}
