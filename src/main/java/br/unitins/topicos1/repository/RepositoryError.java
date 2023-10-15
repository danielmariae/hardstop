package br.unitins.topicos1.repository;

import br.unitins.topicos1.application.ErrorTP1;

public class RepositoryError extends ErrorTP1{

    record FieldError(String fieldName, String message) {}
    private FieldError error;

    public void setFieldError(String fieldName, String message){
        this.error = new FieldError(fieldName, message);
    }

    public FieldError getFieldError() {
        return error;
    }

    public RepositoryError(String code, String message) {
        super(code, message);
    }
    
}
