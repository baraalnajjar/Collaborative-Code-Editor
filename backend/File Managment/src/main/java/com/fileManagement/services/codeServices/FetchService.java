package com.fileManagement.services.codeServices;

import com.fileManagement.entities.MetaData;
import com.fileManagement.entities.Project;
import com.fileManagement.repositories.MetadDataRepository;
import com.fileManagement.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
public class FetchService {

    private final ProjectRepository projectRepository;

    private final MetadDataRepository metadDataRepository;

    public FetchService(ProjectRepository projectRepository, MetadDataRepository metadDataRepository) {
        this.projectRepository = projectRepository;
        this.metadDataRepository = metadDataRepository;
    }

    public ResponseEntity<Map<String, Object>> fetchCode(String projectName) {
        Map<String, Object> response = getCode(projectName);

        if (response.containsKey("error")) {
            return ResponseEntity.status((Integer) response.get("status"))
                    .body(Map.of("error", response.get("error")));
        }

        return ResponseEntity.ok(response);
    }

    public Map<String, Object> getCode(String projectName) {
        Map<String, Object> response = new HashMap<>();

        Project project = projectRepository.findByName(projectName);
        if (project == null) {
            response.put("error", "Project not found: " + projectName);
            response.put("status", 404);
            return response;
        }

        MetaData metaData = metadDataRepository.findByName(projectName);
        if (metaData != null) {
            response.put("code", metaData.getCode());
            response.put("version", metaData.getVersion());
            return response;
        } else {
            response.put("error", "Code not found for project: " + projectName);
            response.put("status", 404);
            return response;
        }
    }


}
