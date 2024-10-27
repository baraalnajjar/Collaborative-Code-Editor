package com.fileManagement.services.codeServices.execute;

import com.fileManagement.services.codeServices.execute.impl.JavaExecutionStrategy;
import com.fileManagement.services.codeServices.execute.impl.PythonExecutionStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CodeExecutionStrategyFactory {


    private final Map<String, CodeExecutionStrategy> strategies = new HashMap<>();

    public CodeExecutionStrategyFactory() {
        strategies.put("java", new JavaExecutionStrategy());
        strategies.put("python", new PythonExecutionStrategy());
    }

    public CodeExecutionStrategy getStrategy(String language) {
        return strategies.get(language.toLowerCase());
    }
}
