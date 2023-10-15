package br.unitins.topicos1.repository;

public class RepositoryException extends RuntimeException{

    private String fieldName;

    public RepositoryException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
    
}
