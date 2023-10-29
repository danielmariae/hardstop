package br.unitins.topicos1.model.converterjpa;

import org.jrimum.domkee.pessoa.CEP;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CEPConverter implements AttributeConverter<CEP, String> {
    @Override
    public String convertToDatabaseColumn(CEP cep) {
        if (cep != null) {
            return cep.getCep(); // Converte o objeto CEP para uma string
        }
        return null;
    }

    @Override
    public CEP convertToEntityAttribute(String cepString) {
        if (cepString != null) {
            CEP cep = new CEP();
            cep.setCep(cepString); // Converte a string para um objeto CEP
            return cep;
        }
        return null;
    }
}

