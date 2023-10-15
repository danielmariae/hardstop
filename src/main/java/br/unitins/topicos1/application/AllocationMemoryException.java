package br.unitins.topicos1.application;

public class AllocationMemoryException extends RuntimeException{

    private String fieldName;

    public AllocationMemoryException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
    
}
