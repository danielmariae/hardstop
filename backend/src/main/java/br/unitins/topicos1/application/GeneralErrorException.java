package br.unitins.topicos1.application;

public class GeneralErrorException extends RuntimeException{

    private String subjectName;
    private String code;
    private String codeMessage;

    public GeneralErrorException(String code, String codeMessage, String subjectName, String message) {
        super(message);
        this.subjectName = subjectName;
        this.code = code;
        this.codeMessage = codeMessage;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCode() {
        return code;
    }

    public String getCodeMessage() {
        return codeMessage;
    }
    
}
