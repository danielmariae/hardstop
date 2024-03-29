package br.unitins.topicos1.Formatadores;

import br.unitins.topicos1.validation.ValidationException;

public class FornLogFormatador {
    public static String fornlogformatador(String cnpj) {
        if(cnpj.matches("([0-9]{2}[-./\\s][0-9]{3}[-./\\s][0-9]{3}[-./\\s][0-9]{4}[-./\\s][0-9]{2})")) {
            String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
            return cnpjLimpo;
        } else if(cnpj.matches("[0-9]{14}")) {
            return cnpj;
        }
            throw new ValidationException("cnpj", "O cnpj precisa ter exatamente 14 números e precisa estar em uma fomatação válida! Formatos válidos: 12.345.678/0001-90 ou 12345678000190");  
        }
}
