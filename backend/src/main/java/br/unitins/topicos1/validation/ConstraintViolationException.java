package br.unitins.topicos1.validation;

public class ConstraintViolationException extends RuntimeException{
    private String fieldName;

    public ConstraintViolationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
    
    
}
