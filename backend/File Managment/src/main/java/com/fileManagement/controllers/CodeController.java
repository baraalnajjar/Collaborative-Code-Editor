package com.fileManagement.controllers;

import com.fileManagement.entities.EditLogs;
import com.fileManagement.services.codeServices.FetchService;
import com.fileManagement.services.codeServices.execute.*;
import com.fileManagement.services.logServices.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fileManagement.services.codeServices.EditService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/code")
public class CodeController {
    private final CodeExecutionStrategyFactory strategyFactory;
    private final CodeExecutor executor = new CodeExecutor();
    private final FetchService fetchService;
    private final EditService editService;
    private final LogService logService;

    public CodeController(CodeExecutionStrategyFactory strategyFactory, FetchService fetchService, EditService editService, LogService logService) {
        this.strategyFactory = strategyFactory;
        this.fetchService = fetchService;
        this.editService = editService;
        this.logService = logService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<Map<String, Object>> fetchCode(@RequestParam String projectName) {
        return fetchService.fetchCode(projectName);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, String>> editCode(@RequestParam String projectName, @RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        String version = requestBody.get("version");
        String username = requestBody.get("username");
        logService.logEdit(username,projectName,version);
        return editService.editCode(projectName, code, version);
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeCode(@RequestParam String language,@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        String className = requestBody.get("className");
        try {
            CodeExecutionStrategy strategy = strategyFactory.getStrategy(language);
            if (strategy == null) {
                return ResponseEntity.badRequest().body("Unsupported language: " + language);
            }
            executor.setStrategy(strategy);
            String output = executor.executeCode( className , code);
            return ResponseEntity.ok(output);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/revert")
    public ResponseEntity<Map<String, String>> revertCode(@RequestParam String projectName, @RequestBody  Map<String, String> requestBody) {
        String id = requestBody.get("projectId");
        String old_version = requestBody.get("old_version");
        String username = requestBody.get("username");

        logService.logRevert(username,projectName,old_version);
        return editService.revertCode(projectName, id,old_version);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<Map<String, String>>> getLogsByName(@RequestParam String projectName) {
        List<EditLogs> logs = logService.getLogsByName(projectName);

        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, String>> logsList = logs.stream()
                .map(log -> Map.of(
                        "username", log.getUsername(),
                        "date", log.getDate(),
                        "time", log.getTime(),
                        "version", log.getVersion(),
                        "editType", log.getEditType().name().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(logsList);
    }

}

