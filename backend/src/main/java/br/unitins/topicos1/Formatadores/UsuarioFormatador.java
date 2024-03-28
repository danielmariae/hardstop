package br.unitins.topicos1.Formatadores;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.unitins.topicos1.validation.ValidationException;

public class UsuarioFormatador {

    public static LocalDate formataDataNascimento(String dataNascimento) {
        
      if(dataNascimento.matches("[0-9]{2}[-/.\s][0-9]{2}[-/.\s][0-9]{4}")) {
        String[] datasplit = dataNascimento.split("[-/.\s]");
        String data = datasplit[2].concat("-").concat(datasplit[1]).concat("-").concat(datasplit[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data, formatter);
        return date;
    } else if(dataNascimento.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
        LocalDate date = LocalDate.parse(dataNascimento);
        return date;
    } else if(dataNascimento.matches("[0-9]{4}[/.\s][0-9]{2}[/.\s][0-9]{2}")) {
        String[] datasplit = dataNascimento.split("[/.\s]");
        String data = datasplit[0].concat("-").concat(datasplit[1]).concat("-").concat(datasplit[2]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data, formatter);
        return date;
    } else if(dataNascimento.matches("[0-9]{8}")){
      String ano = dataNascimento.substring(0, 4);
      String mes = dataNascimento.substring(4, 6);
      String dia = dataNascimento.substring(6);
      String data = ano.concat("-").concat(mes).concat("-").concat(dia);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate date = LocalDate.parse(data, formatter);
      return date;
      } else {
      throw new ValidationException("dataDeNascimento", "Digite uma data de nascimento em um formato válido"); 
      }
    }

public static String formataCpf(String cpf) {
    if(cpf.matches("[0-9]{3}[-/.\s][0-9]{3}[-/.\s][0-9]{3}[-/.\s][0-9]{2}")) {
        // String parte1cpf, parte2cpf, parte3cpf, parte4cpf;
        // parte1cpf = cpf.substring(0,3);
        // parte2cpf = cpf.substring(4,7);
        // parte3cpf = cpf.substring(8,11);
        // parte4cpf = cpf.substring(12,14);
        // return (parte1cpf.concat(parte2cpf).concat(parte3cpf).concat(parte4cpf));
        return cpf.replaceAll("[^0-9]", "");
      } else if(cpf.matches("[0-9]{11}")) {
      return cpf;
      }
      throw new ValidationException("cpf", "O cpf precisa ter exatamente 11 números!");
}




}
