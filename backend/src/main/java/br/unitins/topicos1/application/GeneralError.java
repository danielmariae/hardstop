package br.unitins.topicos1.application;

public class GeneralError extends ErrorTP1{

    record SubjectError(String subjectName, String message) {}
    private SubjectError error;

    public void setSubjectError(String subjectName, String message){
        this.error = new SubjectError(subjectName, message);
    }

    public SubjectError getSubjectError() {
        return error;
    }

    public GeneralError(String code, String message) {
        super(code, message);
    }
    
}
