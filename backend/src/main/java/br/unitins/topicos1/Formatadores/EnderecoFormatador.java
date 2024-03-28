package br.unitins.topicos1.Formatadores;

import br.unitins.topicos1.validation.ValidationException;

public class EnderecoFormatador {
    public static String formataCep(String cep) {
        if(cep.matches(("[0-9]{5}[-/.\s][0-9]{3}"))) {
          // String[] datasplit = cep.split("[-/.\s]");
          // String data = datasplit[0].concat(datasplit[1]);
          String data = cep.replaceAll("[^0-9]", "");
           return data;
         } else if(cep.matches(("[0-9]{8}"))) {
           return cep;
         } else {
            throw new ValidationException("cep", "O cep precisa ter exatamente 5 números e um formato válido!");      
        }
    }
}
