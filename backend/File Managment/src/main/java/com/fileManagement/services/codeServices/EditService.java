package com.fileManagement.services.codeServices;

import com.fileManagement.entities.Backup;
import com.fileManagement.entities.MetaData;
import com.fileManagement.entities.Project;
import com.fileManagement.repositories.BackupRepository;
import com.fileManagement.repositories.MetadDataRepository;
import com.fileManagement.repositories.ProjectRepository;
import com.fileManagement.services.projectServices.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.ServiceUI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class EditService {

    private final ProjectRepository projectRepository;
    private final MetadDataRepository metadDataRepository;
    private final BackupRepository backupRepository;

    public EditService(ProjectRepository projectRepository, MetadDataRepository metadDataRepository, BackupRepository backupRepository) {
        this.projectRepository = projectRepository;
        this.metadDataRepository = metadDataRepository;
        this.backupRepository = backupRepository;
    }
    public ResponseEntity<Map<String, String>> editCode(String projectName, String code, String version) {
        Project project = projectRepository.findByName(projectName);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Project not found: " + projectName));
        }

        if (Objects.equals(project.getVersion(), version)) {
            MetaData metaData = metadDataRepository.findByName(projectName);
            Backup backup = new Backup();
            backup.setProjectName(projectName);
            backup.setCode(metaData.getCode());
            backup.setVersion(metaData.getVersion());
            backupRepository.save(backup);

            metaData.setCode(code);
            metaData.setVersion(incrementVersion(version));
            metadDataRepository.save(metaData);

            project.setVersion(incrementVersion(version));
            projectRepository.save(project);

            return ResponseEntity.ok(Map.of("message", "Code saved successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Someone edited the code, please fetch again first"));
        }
    }
    private String incrementVersion(String version) {
        try {
            double newVersion = Double.parseDouble(version);
            return String.valueOf(Math.round((newVersion + 0.1) * 10.0) / 10.0);
        } catch (NumberFormatException e) {
            return version;
        }
    }
    public ResponseEntity<Map<String, String>> revertCode(String projectName,String id,String old_version) {
        Backup backup = backupRepository.findByProjectNameAndVersion(projectName,old_version);
        Map<String, String> response = new HashMap<>();

        if (backup == null) {
            response.put("error", "Project or version not found");
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println(id);
        Project project= projectRepository.findByid(Long.valueOf(id));


        String old_Code = backup.getCode();

        return editCode(projectName, old_Code, project.getVersion());

    }
}
