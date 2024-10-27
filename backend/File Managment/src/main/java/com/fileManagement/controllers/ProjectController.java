package com.fileManagement.controllers;

import com.fileManagement.services.projectServices.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @GetMapping("/languages")
    public Map<String, Object> getLanguages() {
        return projectService.getLanguages();
    }


    @GetMapping()
    public Map<String, Object> getProjects(@RequestParam String language) {
        return projectService.getProjectsByLanguage(language);
    }

    @GetMapping("/getId")
    public Map<String, Object> getProjectsId(@RequestParam String project_name) {
        return projectService.getProjectIdByName(project_name);
    }
    @GetMapping("/getCode")
    public Map<String, Object> getProjectCode(@RequestParam String project_name) {
        return projectService.getProjectCodeByName(project_name);
    }


    @GetMapping("/{projectName}/versions")
    public ResponseEntity<List<String>> getAllVersions(@PathVariable String projectName) {
        return projectService.getAllVersions(projectName);
    }

    @PostMapping("/create")
    public ResponseEntity<List<String>> createProject(@RequestParam String projectName, @RequestBody Map<String, String> requestBody) {
        return projectService.createProject(projectName, requestBody);
    }

}
