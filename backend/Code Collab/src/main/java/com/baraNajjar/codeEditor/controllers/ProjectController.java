package com.baraNajjar.codeEditor.controllers;


import com.baraNajjar.codeEditor.services.CodeService;
import com.baraNajjar.codeEditor.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {


    private final ProjectService projectService;
    private final CodeService codeService;
    public ProjectController(ProjectService projectService, CodeService codeService) {
        this.projectService = projectService;
        this.codeService = codeService;
    }

    @GetMapping("/languages")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> getLanguages() {
        Map<String, Object> response = projectService.getLanguages();
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> getProjectsByLanguage(@RequestParam String language) {
        Map<String, Object> response = projectService.getProjectsByLanguage(language);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getId")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> getProjectsByName(@RequestParam String project_name) {
        Map<String, Object> response = projectService.getID(project_name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCode")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> getProjectCode(@RequestParam String project_name) {
        Map<String, Object> response = projectService.getCode(project_name);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('EDITOR')")
    public ResponseEntity<Map<String, String>> editCode(@RequestParam String projectName , @RequestBody Map<String, String> requestBody) {
        return codeService.editCode(projectName,requestBody);
    }

    @PostMapping("/execute")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<String> execute(@RequestParam String language , @RequestBody Map<String, String> requestBody) {
        return codeService.executeCode(language,requestBody);
    }

    @GetMapping("/{projectName}/versions")
    @PreAuthorize("hasAnyAuthority('EDITOR')")
    public ResponseEntity<List<String>> getAllVersions(@PathVariable String projectName) {
        return projectService.getAllVersions(projectName);
    }

    @PostMapping("/revert")
    @PreAuthorize("hasAnyAuthority('EDITOR')")
    public ResponseEntity<Map<String, String>> revertCode(@RequestParam String projectName, @RequestBody  Map<String, String> requestBody) {
        return codeService.revertCode(projectName,requestBody);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('EDITOR')")
    public ResponseEntity<List<String>> createProject(@RequestParam String projectName, @RequestBody  Map<String, String> requestBody) {
        return projectService.createProject(projectName, requestBody);
    }
    @GetMapping("/logs")
    @PreAuthorize("hasAnyAuthority('VIEWER')")
    public ResponseEntity<List<Map<String, String>>> getLogsByProjectName(@RequestParam String projectName) {
        return codeService.getLogsByProjectName(projectName);
    }

}
