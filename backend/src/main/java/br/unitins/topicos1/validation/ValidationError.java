package br.unitins.topicos1.validation;

import java.util.ArrayList;
import java.util.List;

import br.unitins.topicos1.application.ErrorTP1;

public class ValidationError extends ErrorTP1 {

    record FieldError(String fieldName, String message) {};
    private List<FieldError> errors = null;

    public ValidationError(String code, String message) {
        super(code, message);
    }

    public void addFieldError(String fieldName, String message) {
        if(errors == null)
        errors = new ArrayList<FieldError>();

        errors.add(new FieldError(fieldName, message));
    }

    public List<FieldError> getErrors() {
        return errors.stream().toList();
    }
    
}
