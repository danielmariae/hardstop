package br.unitins.topicos1.validation;

// public class ConstraintViolationException extends RuntimeException{
//     private String fieldName;

//     public ConstraintViolationException(String fieldName, String message) {
//         super(message);
//         this.fieldName = fieldName;
//     }

//     public String getFieldName() {
//         return fieldName;
//     }
    
    
// }

public class ConstraintViolationException extends RuntimeException {
    private String entityName;
    private String message;

    public ConstraintViolationException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.message = message;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

