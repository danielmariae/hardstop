package br.unitins.topicos1.application;

public class AllocationMemoryError extends ErrorTP1{

    record FieldError(String fieldName, String message) {}
    private FieldError error;

    public void setFieldError(String fieldName, String message){
        this.error = new FieldError(fieldName, message);
    }

    public FieldError getFieldError() {
        return error;
    }

    public AllocationMemoryError(String code, String message) {
        super(code, message);
    }
    
}
