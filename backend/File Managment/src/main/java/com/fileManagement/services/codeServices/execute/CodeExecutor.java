package com.fileManagement.services.codeServices.execute;

public class CodeExecutor {
    private CodeExecutionStrategy strategy;

    public void setStrategy(CodeExecutionStrategy strategy) {
        this.strategy = strategy;
    }

    public String executeCode(String className , String code) throws Exception {
        if (strategy == null) {
            throw new IllegalStateException("No execution strategy set.");
        }
        return strategy.execute( className ,  code);
    }
}
