package com.fileManagement.services.codeServices.execute;

public interface CodeExecutionStrategy {
    String execute(String className , String code) throws Exception;
}
