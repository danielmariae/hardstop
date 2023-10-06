package br.unitins.topicos1.Formatadores;

public class TelefoneFormatador {
    public static String validaNumeroTelefone(String numeroTelefone) {
        if(numeroTelefone.matches(("[0-9]{5}[-/.\s][0-9]{3}"))) {
            String[] datasplit = numeroTelefone.split("[-/.\s]");
           String data = datasplit[0].concat(datasplit[1]);
           return data;
         } else if(numeroTelefone.matches(("[0-9]{9}")))    {
           return numeroTelefone;
         } else {
            return null;
         }
    }
}
