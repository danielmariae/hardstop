package br.unitins.topicos1.Formatadores;

import br.unitins.topicos1.validation.ValidationException;

public class TelefoneFormatador {
    public static String formataNumeroTelefone(String numeroTelefone) {
        if(numeroTelefone.matches(("[0-9]{5}[-/.\s][0-9]{4}"))) {
          // String[] datasplit = numeroTelefone.split("[-/.\s]");
          // String data = datasplit[0].concat(datasplit[1]);
          String data = numeroTelefone.replaceAll("[^0-9]", "");
           return data;
         } else if(numeroTelefone.matches(("[0-9]{9}")))    {
           return numeroTelefone;
         } else {
              throw new ValidationException("numeroTelefone", "O número de telefone precisa ter exatamente 9 números e precisa ter fomatação válida! Formatação válida: 97865-1234 ou 978651234");  
         }
    }
}
