package br.unitins.topicos1.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import br.unitins.topicos1.model.Pix;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GerarPix {
    public static void QrCodePix(Pix pagamento) {
    String filePath = "pix.png"; // Caminho para a imagem de saída

    Integer mai = (pagamento.getChaveRecebedor().length() + 22);
    Integer chave = pagamento.getChaveRecebedor().length();

    Integer valor = pagamento.getValorPago().toString().length();
    String valorF = valor.toString();
    if (valor < 10) {
      // Os campos com tamanho menor que 10 precisam ser representados com um zero na frente 
      valorF = "0" + valorF;
    }

    // Merchant Name até 25 caracteres.
    String nomeRecebedor;
    Integer nomeRecebedorTam;
    if (pagamento.getNomeRecebedor().length() > 25) {
      nomeRecebedor = pagamento.getNomeRecebedor().substring(0, 25);
      nomeRecebedorTam = nomeRecebedor.length();
    } else {
      nomeRecebedor = pagamento.getNomeRecebedor();
      nomeRecebedorTam = pagamento.getNomeRecebedor().length();
    }

    String nomeRecebedorF = nomeRecebedorTam.toString();
    if (nomeRecebedorTam < 10) {
      // Os campos com tamanho menor que 10 precisam ser representados com um zero na frente 
      nomeRecebedorF = "0" + nomeRecebedorF;
    }

    // City Name até 15 caracteres.
    String cidadeNome;
    Integer cidadeTam;
    if (pagamento.getNomeCidade().length() > 15) {
      cidadeNome = pagamento.getNomeCidade().substring(0, 15);
      cidadeTam = cidadeNome.length();
    } else {
      cidadeNome = pagamento.getNomeCidade();
      cidadeTam = pagamento.getNomeCidade().length();
    }
    String cidadeF = cidadeTam.toString();

    if (cidadeTam < 10) {
      // Os campos com tamanho menor que 10 precisam ser representados com um zero na frente 
      cidadeF = "0" + cidadeF;
    }

    // O txid deve conter até 25 caracteres e respeitar a seguinte regra: a-z; A-Z; 0-9.
    String nomeCliente = pagamento.getNomeCliente();
    String nomeF = "";
    if (nomeCliente.matches(".+\\S+.*")) {
      String[] datasplit = nomeCliente.split("\\s+");
      for (int i = 0; i < datasplit.length; i++) {
        nomeF = nomeF + datasplit[i];
      }
    }

    if (nomeF != "") {
      if (nomeF.length() > 10) {
        nomeCliente = nomeF.substring(0, 10);
      } else {
        nomeCliente = nomeF;
      }
    }

    String[] datasplit = pagamento
      .getDataHoraGeracao()
      .toString()
      .split("[-:.]");
    String dataHora =
      datasplit[0] + datasplit[1] + datasplit[2] + datasplit[3] + datasplit[4];
    String txId = nomeCliente + dataHora;
    Integer txIdTam = txId.length();
    Integer adft = txIdTam + 4;

    String data =
      "000201" +
      "26" +
      mai.toString() +
      "0014br.gov.bcb.pix" +
      "01" +
      chave.toString() +
      pagamento.getChaveRecebedor() +
      "52040000" +
      "5303986" +
      "54" +
      valorF +
      pagamento.getValorPago().toString() +
      "5802BR" +
      "59" +
      nomeRecebedorF +
      nomeRecebedor +
      "60" +
      cidadeF +
      cidadeNome +
      "62" +
      adft.toString() +
      "05" +
      txIdTam.toString() +
      txId +
      "6304";

    int crc16 = CRC16Calculator.calculateCRC16(data);

    String dadosFinais = data + String.format("%04X", crc16);

    int width = 300;
    int height = 300;
    String fileType = "png";

    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    try {
      BitMatrix matrix = new MultiFormatWriter()
        .encode(
          new String(dadosFinais.getBytes("UTF-8"), "CP850"),
          BarcodeFormat.QR_CODE,
          width,
          height,
          hintMap
        );

      /* QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(dadosPagamento, BarcodeFormat.QR_CODE, width, height, hintMap); */
        
        Path path = FileSystems.getDefault().getPath(filePath);

      MatrixToImageWriter.writeToPath(
        matrix,
        fileType,
        path
      );

      File arquivo = path.toFile();
      mostrePixNaTela(arquivo);

    } catch (WriterException | IOException e) {
      e.printStackTrace();
    }
  }

  private static void mostrePixNaTela(File arquivoBoleto) {

    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

    try {
            desktop.open(arquivoBoleto);
    } catch (IOException e) {
            e.printStackTrace();
    }
}



  public class CRC16Calculator {

    // Tabela de lookup para o cálculo do CRC16 (CRC-CCITT)
    private static final int[] CRC16_TABLE = {
      0x0000,
      0x1021,
      0x2042,
      0x3063,
      0x4084,
      0x50A5,
      0x60C6,
      0x70E7,
      0x8108,
      0x9129,
      0xA14A,
      0xB16B,
      0xC18C,
      0xD1AD,
      0xE1CE,
      0xF1EF,
    };

    public static int calculateCRC16(String data) {
      int crc = 0xFFFF; // Valor inicial do CRC16

      for (int i = 0; i < data.length(); i++) {
        crc =
          (crc << 4) ^
          CRC16_TABLE[((crc >>> 12) ^ (data.charAt(i) >> 4)) & 0x0F];
        crc =
          (crc << 4) ^
          CRC16_TABLE[((crc >>> 12) ^ (data.charAt(i) & 0x0F)) & 0x0F];
      }

      return crc & 0xFFFF; // Garante que o resultado seja um valor de 16 bits
    }
  }
}
