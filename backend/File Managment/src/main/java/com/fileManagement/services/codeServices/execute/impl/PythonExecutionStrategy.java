package com.fileManagement.services.codeServices.execute.impl;

import com.fileManagement.services.codeServices.execute.CodeExecutionStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class PythonExecutionStrategy implements CodeExecutionStrategy {
    @Override
    public String execute(String scriptName, String code) throws Exception {
        Path tempDir = Files.createTempDirectory("python_code_temp");

        try {
            Path pythonFile = tempDir.resolve("script.py");

            try (BufferedWriter writer = Files.newBufferedWriter(pythonFile)) {
                writer.write(code);
            }

            Path dockerFile = tempDir.resolve("Dockerfile");
            String dockerfileContent = "FROM python:3.9-slim\n" +
                    "WORKDIR /app\n" +
                    "COPY script.py .\n" +
                    "CMD [\"python\", \"script.py\"]";

            try (BufferedWriter writer = Files.newBufferedWriter(dockerFile)) {
                writer.write(dockerfileContent);
            }

            // Build the Docker image
            ProcessBuilder buildProcessBuilder = new ProcessBuilder("docker", "build", "-t", "python_code_runner", ".");
            buildProcessBuilder.directory(tempDir.toFile());
            buildProcessBuilder.redirectErrorStream(true);
            Process buildProcess = buildProcessBuilder.start();
            String buildOutput = readOutput(buildProcess.getInputStream());
            int buildExitCode = buildProcess.waitFor();

            if (buildExitCode != 0) {
                return "Docker build failed:\n" + buildOutput;
            }

            ProcessBuilder runProcessBuilder = new ProcessBuilder("docker", "run", "--rm", "python_code_runner");
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            String output = readOutput(runProcess.getInputStream());
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
