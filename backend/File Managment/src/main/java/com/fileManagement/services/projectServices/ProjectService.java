package com.fileManagement.services.projectServices;

import com.fileManagement.entities.Backup;
import com.fileManagement.entities.EditLogs;
import com.fileManagement.entities.MetaData;
import com.fileManagement.entities.Project;
import com.fileManagement.enums.Languages;
import com.fileManagement.repositories.BackupRepository;
import com.fileManagement.repositories.EditLogsRepository;
import com.fileManagement.repositories.MetadDataRepository;
import com.fileManagement.repositories.ProjectRepository;
import com.fileManagement.services.projectServices.create.ProjectCreationStrategy;
import com.fileManagement.services.projectServices.create.ProjectCreationStrategyFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MetadDataRepository metadDataRepository;
    private final BackupRepository backupRepository;
    private final ProjectCreationStrategyFactory projectCreationStrategyFactory;
    public ProjectService(ProjectRepository projectRepository, MetadDataRepository metadDataRepository,
                          BackupRepository backupRepository, ProjectCreationStrategyFactory projectCreationStrategyFactory){
        this.projectRepository = projectRepository;
        this.metadDataRepository = metadDataRepository;
        this.backupRepository = backupRepository;
        this.projectCreationStrategyFactory = projectCreationStrategyFactory;
    }
    public Map<String, Object> getProjectsByLanguage(String language) {
        List<Project> projects = projectRepository.findByLanguage(language);
        List<String> projectNames = projects.stream()
                .map(Project::getName)
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("projects", projectNames);
        return response;
    }
    public Map<String, Object> getProjectIdByName(String projectName) {
        Project project = projectRepository.findByName(projectName);
        Map<String, Object> response = new HashMap<>();
        if (project != null) {
            response.put("id", project.getId());
        } else {
            response.put("error", "Project not found");
        }
        return response;
    }
    public Map<String, Object> getProjectCodeByName(String projectName) {
        MetaData metaData = metadDataRepository.findByName(projectName);
        Map<String, Object> response = new HashMap<>();
        if (metaData != null) {
            response.put("code", metaData.getCode());
            response.put("version", metaData.getVersion());

        } else {
            response.put("error", "Project not found");
        }
        return response;
    }
    public ResponseEntity<List<String>> getAllVersions( String projectName) {
        List<Backup> backups = backupRepository.findByProjectName(projectName);
        List<String> versions = backups.stream()
                .map(Backup::getVersion)
                .collect(Collectors.toList());

        if (versions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(versions);
    }
    public Map<String, Object> getLanguages() {
        return Arrays.stream(Languages.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        lang -> lang == Languages.CPP ? "C++" : lang.name()
                ));
    }
    public ResponseEntity<List<String>> createProject(String projectName, Map<String, String> requestBody) {
        List<String> response = new ArrayList<>();
        String language = requestBody.get("language");
        String username = requestBody.get("username");

        ProjectCreationStrategy strategy = projectCreationStrategyFactory.getStrategy(language);

        if (strategy == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonList("Unsupported language: " + language));
        }

        try {
            String projectCode = strategy.createProject(projectName,username);
            response.add(projectCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("Error creating project: " + e.getMessage()));
        }
    }



}