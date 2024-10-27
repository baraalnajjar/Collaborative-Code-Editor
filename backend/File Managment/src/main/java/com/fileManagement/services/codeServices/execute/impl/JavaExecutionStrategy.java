package com.fileManagement.services.codeServices.execute.impl;
import com.fileManagement.services.codeServices.execute.CodeExecutionStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaExecutionStrategy implements CodeExecutionStrategy {
    @Override
    public String execute(String className , String code) throws Exception {
        String output;
        Path tempDir = Files.createTempDirectory("java_code_temp");

        try {
            Path javaFile = tempDir.resolve(className + ".java");

            try (BufferedWriter writer = Files.newBufferedWriter(javaFile)) {
                writer.write(code);
            }

            ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", javaFile.toString());
            compileProcessBuilder.redirectErrorStream(true);
            Process compileProcess = compileProcessBuilder.start();
            String compileOutput = readOutput(compileProcess.getInputStream());
            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode != 0) {
                return "Compilation failed:\n" + compileOutput;
            }

            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", tempDir.toString(), className);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            output = readOutput(runProcess.getInputStream());
            int runExitCode = runProcess.waitFor();

            if (runExitCode != 0) {
                return "Execution failed:\n" + output;
            }

            return output;

        } finally {
            if (tempDir != null) {
                Files.walk(tempDir)
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
        }
    }

    private String readOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }
}
