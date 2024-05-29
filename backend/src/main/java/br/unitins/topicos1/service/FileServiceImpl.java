package br.unitins.topicos1.service;

import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.cliente.ClienteResponseDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.produto.ProdutoResponseDTO;
import br.unitins.topicos1.model.produto.Produto;
import br.unitins.topicos1.model.utils.Cliente;
import br.unitins.topicos1.model.utils.Funcionario;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.FuncionarioRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FileServiceImpl implements FileService {

  @Inject
  ClienteRepository repositoryCliente;

  @Inject
  FuncionarioRepository repositoryFuncionario;

  @Inject
  ProdutoRepository repositoryProduto;

  private final String PATH_USER_IMAGE =
    System.getProperty("user.home") +
    File.separator +
    "quarkus" +
    File.separator +
    "images" +
    File.separator +
    "cliente" +
    File.separator;

  private final String PATH_PRODUTO_IMAGE =
    System.getProperty("user.home") +
    File.separator +
    "quarkus" +
    File.separator +
    "images" +
    File.separator +
    "produtos" +
    File.separator;

  private final String PATH_USER_DOCUMENTS =
    System.getProperty("user.home") +
    File.separator +
    "quarkus" +
    File.separator +
    "documents" +
    File.separator +
    "cliente" +
    File.separator;

  private final String PATH_USER_VIDEOS =
    System.getProperty("user.home") +
    File.separator +
    "quarkus" +
    File.separator +
    "videos" +
    File.separator +
    "cliente" +
    File.separator;

  private static final String PATH_USER_PIX =
    System.getProperty("user.home") +
    File.separator +
    "quarkus" +
    File.separator +
    "pix" +
    File.separator +
    "cliente" +
    File.separator;

  private static final String PATH_USER_BOLETO =
    System.getProperty("user.home") +
    File.separator +
    "quarkus" +
    File.separator +
    "boleto" +
    File.separator +
    "cliente" +
    File.separator;

  private static final List<String> SUPPORTED_IMAGE_TYPES = Arrays.asList(
    "image/jpeg",
    "image/jpg",
    "image/png",
    "image/gif",
    "image/svg",
    "image/bmp",
    "image/tiff"
  );

  private static final List<String> SUPPORTED_DOCUMENT_TYPES = Arrays.asList(
    "doc/docx",
    "doc/doc",
    "doc/odt",
    "doc/xml",
    "doc/json",
    "doc/rtf",
    "doc/txt",
    "doc/pptx",
    "doc/pdf",
    "doc/ppt",
    "doc/xlsx",
    "doc/xls",
    "doc/csv",
    "doc/ods"
  );

  private static final List<String> SUPPORTED_VIDEO_TYPES = Arrays.asList(
    "video/mp4",
    "video/mov",
    "video/wmv",
    "video/avi",
    "video/flv",
    "video/mkv",
    "video/webm",
    "video/swf",
    "video/f4v",
    "video/ogg"
  );

  private static final int MAX_IMAGE_FILE_SIZE = 1024 * 1024 * 10; // 10Mb

  private static final int MAX_DOCUMENT_FILE_SIZE = 1024 * 1024 * 20; // 20Mb

  private static final int MAX_VIDEO_FILE_SIZE = 1024 * 1024 * 40; // 40Mb

  private void verificarTamanhoImagem(byte[] imagem) throws IOException {
    if (imagem.length > MAX_IMAGE_FILE_SIZE) throw new IOException(
      "Arquivo maior que 10mb."
    );
  }

  private void verificarTamanhoDocumento(byte[] documento) throws IOException {
    if (documento.length > MAX_DOCUMENT_FILE_SIZE) throw new IOException(
      "Arquivo maior que 20mb."
    );
  }

  private void verificarTamanhoVideo(byte[] video) throws IOException {
    if (video.length > MAX_VIDEO_FILE_SIZE) throw new IOException(
      "Arquivo maior que 40mb."
    );
  }

  @Override
  public String salvarU(String nomeArquivo, byte[] arquivo) throws IOException {
    String archiveType = Files.probeContentType(Paths.get(nomeArquivo));
    if (SUPPORTED_IMAGE_TYPES.contains(archiveType)) {
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
      try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
        fos.write(arquivo);
        // return filePath.toString();
        return filePath.toFile().getName();
      }
    } else if (SUPPORTED_DOCUMENT_TYPES.contains(archiveType)) {
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
      try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
        fos.write(arquivo);
        // return filePath.toString();
        return filePath.toFile().getName();
      }
    } else if (SUPPORTED_VIDEO_TYPES.contains(archiveType)) {
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
      try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
        fos.write(arquivo);
        // return filePath.toString();
        return filePath.toFile().getName();
      }
    } else {
      throw new IOException("Tipo de arquivo não suportado.");
    }
  }

  public StreamingOutput obterU(String nomeArquivo) {
    try {
      String archiveType = Files.probeContentType(Paths.get(nomeArquivo));
      if (SUPPORTED_IMAGE_TYPES.contains(archiveType)) {
        File file = new File(PATH_USER_IMAGE + nomeArquivo);
        if (!file.exists()) {
          throw new GeneralErrorException(
            "400",
            "Bad Request",
            "FileServiceImpl(obterU)",
            "Este arquivo inexiste no sistema."
          );
        }

        StreamingOutput stream = new StreamingOutput() {
          @Override
          public void write(OutputStream output) throws IOException {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
              byte[] buffer = new byte[4096];
              int length;
              while ((length = fileInputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
              }
            }
          }
        };

        return stream;
      } else if (SUPPORTED_DOCUMENT_TYPES.contains(archiveType)) {
        File file = new File(PATH_USER_DOCUMENTS + nomeArquivo);
        if (!file.exists()) {
          throw new GeneralErrorException(
            "400",
            "Bad Request",
            "FileServiceImpl(obterU)",
            "Este arquivo inexiste no sistema."
          );
        }

        StreamingOutput stream = new StreamingOutput() {
          @Override
          public void write(OutputStream output) throws IOException {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
              byte[] buffer = new byte[4096];
              int length;
              while ((length = fileInputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
              }
            }
          }
        };

        return stream;
      } else if (SUPPORTED_VIDEO_TYPES.contains(archiveType)) {
        File file = new File(PATH_USER_VIDEOS + nomeArquivo);
        if (!file.exists()) {
          throw new GeneralErrorException(
            "400",
            "Bad Request",
            "FileServiceImpl(obterU)",
            "Este arquivo inexiste no sistema."
          );
        }

        StreamingOutput stream = new StreamingOutput() {
          @Override
          public void write(OutputStream output) throws IOException {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
              byte[] buffer = new byte[4096];
              int length;
              while ((length = fileInputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
              }
            }
          }
        };

        return stream;
      } else {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "FileServiceImpl(obter)",
          "Tipo de arquivo não suportado"
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Transactional
  public ClienteResponseDTO updateNomeImagemC(Long id, String nomeImagem) {
    Cliente cliente = repositoryCliente.findById(id);

    if (cliente.getNomeImagem() != null) {
      String spath = PATH_USER_IMAGE + cliente.getNomeImagem();
      Path path = Paths.get(spath);
      try {
        Files.delete(path);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "FileServiceImpl(updateNomeImagemC)",
          "Não consegui excluir o arquivo"
        );
      }
    }

    // Adicionando o nome do arquivo de imagem
    cliente.setNomeImagem(nomeImagem);

    return ClienteResponseDTO.valueOf(cliente);
  }

  @Transactional
  public ProdutoResponseDTO updateNomeImagemP(Long id, String nomeImagem) {
    Produto produto = repositoryProduto.findById(id);

    if (produto.getNomeImagem() == null) produto.setNomeImagem(
      new ArrayList<String>()
    );
    produto.getNomeImagem().add(nomeImagem);
    return ProdutoResponseDTO.valueOf(produto);
  }

  @Transactional
  public FuncionarioResponseDTO updateNomeImagemF(Long id, String nomeImagem) {
    Funcionario funcionario = repositoryFuncionario.findById(id);

    if (funcionario.getNomeImagem() != null) {
      String spath = PATH_USER_IMAGE + funcionario.getNomeImagem();
      Path path = Paths.get(spath);
      try {
        Files.delete(path);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "FileServiceImpl(updateNomeImagemC)",
          "Não consegui excluir o arquivo"
        );
      }
    }

    // Adicionando o nome do arquivo de imagem
    funcionario.setNomeImagem(nomeImagem);

    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  public String salvarP(String nomeArquivo, byte[] arquivo) throws IOException {
    String archiveType = Files.probeContentType(Paths.get(nomeArquivo));
    if (SUPPORTED_IMAGE_TYPES.contains(archiveType)) {
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
      try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
        fos.write(arquivo);
        // return filePath.toString();
        return filePath.toFile().getName();
      }
    } else {
      throw new IOException("Tipo de imagem não suportada.");
    }
  }

  public StreamingOutput obterP(String nomeArquivo) {
    File file = new File(PATH_PRODUTO_IMAGE + nomeArquivo);

    if (!file.exists()) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "FileServiceImpl(obterP)",
        "Este arquivo inexiste no sistema."
      );
    }

    StreamingOutput stream = new StreamingOutput() {
      @Override
      public void write(OutputStream output) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
          byte[] buffer = new byte[4096];
          int length;
          while ((length = fileInputStream.read(buffer)) != -1) {
            output.write(buffer, 0, length);
          }
        }
      }
    };

    return stream;
  }

  public static File obterArquivoPix(String nomeArquivo) {
    File file = new File(PATH_USER_PIX + nomeArquivo);

    if (!file.exists()) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "GerarPix(obterArquivoPix)",
        "Este arquivo inexiste no sistema."
      );
    }

    return file;
  }

  public static File obterArquivoBoleto(String nomeArquivo) {
    File file = new File(PATH_USER_BOLETO + nomeArquivo);

    if (!file.exists()) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "GerarBoleto(obterArquivoBoleto)",
        "Este arquivo inexiste no sistema."
      );
    }

    return file;
  }
}
