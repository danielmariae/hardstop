package br.unitins.topicos1.service;

import java.io.File;
import java.io.IOException;

import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;

public interface FileService {
    
    String salvarU(String nomeArquivo, byte[] arquivo) throws IOException;

    String salvarP(String nomeArquivo, byte[] arquivo) throws IOException;

    File obterU(String nomeArquivo);

    File obterP(String nomeArquivo);

    FuncionarioResponseDTO updateNomeImagemF(Long id, String nomeImagem);

    ClienteResponseDTO updateNomeImagemC(Long id, String nomeImagem);

}
