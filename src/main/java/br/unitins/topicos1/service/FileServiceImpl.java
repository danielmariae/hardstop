package br.unitins.topicos1.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import br.unitins.topicos1.application.GeneralErrorException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileServiceImpl implements FileService{
    
    private final String PATH_USER_IMAGE = System.getProperty("user.home") +
    File.separator + "quarkus" +
    File.separator + "images" +
    File.separator + "usuario" +
    File.separator;

    private final String PATH_PRODUTO_IMAGE = System.getProperty("user.home") +
    File.separator + "quarkus" +
    File.separator + "images" +
    File.separator + "produtos" +
    File.separator;

    private final String PATH_USER_DOCUMENTS = System.getProperty("user.home") +
    File.separator + "quarkus" +
    File.separator + "documents" +
    File.separator + "usuario" +
    File.separator;

    private final String PATH_USER_VIDEOS = System.getProperty("user.home") +
    File.separator + "quarkus" +
    File.separator + "videos" +
    File.separator + "usuario" +
    File.separator;

    private static final List<String> SUPPORTED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/gif", "image/svg", "image/bmp", "image/tiff");

    private static final List<String> SUPPORTED_DOCUMENT_TYPES = Arrays.asList("doc/docx", "doc/doc", "doc/odt", "doc/xml", "doc/json", "doc/rtf", "doc/txt", "doc/pptx", "doc/pdf", "doc/ppt", "doc/xlsx", "doc/xls", "doc/csv", "doc/ods");

    private static final List<String> SUPPORTED_VIDEO_TYPES = Arrays.asList("video/mp4", "video/mov", "video/wmv", "video/avi", "video/flv", "video/mkv", "video/webm", "video/swf", "video/f4v");

    private static final int MAX_IMAGE_FILE_SIZE = 1024 * 1024 * 10; // 10Mb

    private static final int MAX_DOCUMENT_FILE_SIZE = 1024 * 1024 * 20; // 20Mb

    private static final int MAX_VIDEO_FILE_SIZE = 1024 * 1024 * 40; // 40Mb

    private void verificarTamanhoImagem(byte[] imagem) throws IOException {
        if(imagem.length > MAX_IMAGE_FILE_SIZE)
            throw new IOException("Arquivo maior que 10mb.");
    }

    private void verificarTamanhoDocumento(byte[] documento) throws IOException {
        if(documento.length > MAX_DOCUMENT_FILE_SIZE)
            throw new IOException("Arquivo maior que 20mb.");
    }

    private void verificarTamanhoVideo(byte[] video) throws IOException {
        if(video.length > MAX_VIDEO_FILE_SIZE)
            throw new IOException("Arquivo maior que 40mb.");
    }

    @Override
    public String salvarU(String nomeArquivo, byte[] arquivo) throws IOException {
        String archiveType = Files.probeContentType(Paths.get(nomeArquivo));
        if(SUPPORTED_IMAGE_TYPES.contains(archiveType)) {
            verificarTamanhoImagem(arquivo);
            
            // criar diretorio caso nao exista
            Path diretorio = Paths.get(PATH_USER_IMAGE);
            Files.createDirectories(diretorio);

            Boolean chave = false;
            String extensao = archiveType.substring(archiveType.lastIndexOf('/') + 1);
            Path filePath;
            do {
                // criando o nome do arquivo randomico
                String novoNomeArquivo = UUID.randomUUID() + "." + extensao;

                // definindo o caminho completo do arquivo
                filePath = diretorio.resolve(novoNomeArquivo);
                chave = filePath.toFile().exists();
            } while (chave);

            // salvar arquivo
            try(FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(arquivo);
                // return filePath.toString();
                return filePath.toFile().getName();
            }

        } else if(SUPPORTED_DOCUMENT_TYPES.contains(archiveType)) {
            verificarTamanhoDocumento(arquivo);
            
            // criar diretorio caso nao exista
            Path diretorio = Paths.get(PATH_USER_DOCUMENTS);
            Files.createDirectories(diretorio);

            Boolean chave = false;
            String extensao = archiveType.substring(archiveType.lastIndexOf('/') + 1);
            Path filePath;
            do {
                // criando o nome do arquivo randomico
                String novoNomeArquivo = UUID.randomUUID() + "." + extensao;

                // definindo o caminho completo do arquivo
                filePath = diretorio.resolve(novoNomeArquivo);
                chave = filePath.toFile().exists();
            } while (chave);

            // salvar arquivo
            try(FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(arquivo);
                // return filePath.toString();
                return filePath.toFile().getName();
            }

        } else if(SUPPORTED_VIDEO_TYPES.contains(archiveType)) {
            verificarTamanhoVideo(arquivo);
            
            // criar diretorio caso nao exista
            Path diretorio = Paths.get(PATH_USER_VIDEOS);
            Files.createDirectories(diretorio);

            Boolean chave = false;
            String extensao = archiveType.substring(archiveType.lastIndexOf('/') + 1);
            Path filePath;
            do {
                // criando o nome do arquivo randomico
                String novoNomeArquivo = UUID.randomUUID() + "." + extensao;

                // definindo o caminho completo do arquivo
                filePath = diretorio.resolve(novoNomeArquivo);
                chave = filePath.toFile().exists();
            } while (chave);

            // salvar arquivo
            try(FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(arquivo);
                // return filePath.toString();
                return filePath.toFile().getName();
            }
        } else {
            throw new IOException("Tipo de arquivo não suportado.");
        }
    }
    
    public File obterU(String nomeArquivo) {
        
            try {
                String archiveType = Files.probeContentType(Paths.get(nomeArquivo));
                if(SUPPORTED_IMAGE_TYPES.contains(archiveType)) {
                    File file = new File(PATH_USER_IMAGE+nomeArquivo);
                    return file;
                } else if(SUPPORTED_DOCUMENT_TYPES.contains(archiveType)) {
                    File file = new File(PATH_USER_DOCUMENTS+nomeArquivo);
                    return file;
                } else if(SUPPORTED_VIDEO_TYPES.contains(archiveType)) {
                    File file = new File(PATH_USER_VIDEOS+nomeArquivo);
                    return file;
                } else {
                    throw new GeneralErrorException("400", "Bad Resquest", "FileServiceImpl(obter)", "Tipo de arquivo não suportado");
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return null;
    }

    @Override
    public String salvarP(String nomeArquivo, byte[] arquivo) throws IOException {
        String archiveType = Files.probeContentType(Paths.get(nomeArquivo));
        if(SUPPORTED_IMAGE_TYPES.contains(archiveType)) {
            verificarTamanhoImagem(arquivo);
            
            // criar diretorio caso nao exista
            Path diretorio = Paths.get(PATH_PRODUTO_IMAGE);
            Files.createDirectories(diretorio);

            Boolean chave = false;
            String extensao = archiveType.substring(archiveType.lastIndexOf('/') + 1);
            Path filePath;
            do {
                // criando o nome do arquivo randomico
                String novoNomeArquivo = UUID.randomUUID() + "." + extensao;

                // definindo o caminho completo do arquivo
                filePath = diretorio.resolve(novoNomeArquivo);
                chave = filePath.toFile().exists();
            } while (chave);

            // salvar arquivo
            try(FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(arquivo);
                // return filePath.toString();
                return filePath.toFile().getName();
            }

        } else {
            throw new IOException("Tipo de imagem não suportada.");
        }
    }

    public File obterP(String nomeArquivo) {
        File file = new File(PATH_PRODUTO_IMAGE+nomeArquivo);
        return file;
    }





}
