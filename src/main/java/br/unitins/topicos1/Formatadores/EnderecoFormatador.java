package br.unitins.topicos1.Formatadores;

public class EnderecoFormatador {
    public static String validaCep(String cep) {
        if(cep.matches(("[0-9]{5}[-/.\s][0-9]{3}"))) {
            String[] datasplit = cep.split("[-/.\s]");
           String data = datasplit[0].concat(datasplit[1]);
           return data;
         } else if(cep.matches(("[0-9]{8}"))) {
           return cep;
         } else {
            return null;         }
    }
}
