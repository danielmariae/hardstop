package br.unitins.topicos1.service;

import java.io.File;
import java.io.IOException;

public interface FileService {
    
    String salvarU(String nomeArquivo, byte[] arquivo) throws IOException;

    String salvarP(String nomeArquivo, byte[] arquivo) throws IOException;

    File obterU(String nomeArquivo);

    File obterP(String nomeArquivo);

}
