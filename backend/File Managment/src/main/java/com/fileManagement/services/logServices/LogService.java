package com.fileManagement.services.logServices;

import com.fileManagement.entities.EditLogs;
import com.fileManagement.enums.EditType;
import com.fileManagement.repositories.EditLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class LogService {

    @Autowired
    private EditLogsRepository editLogsRepository;

    public void logEdit(String username, String project_name, String version) {
        EditLogs editLog = createLogEntry(username, project_name, version, EditType.EDIT);
        editLogsRepository.save(editLog);
    }

    public void logRevert(String username, String project_name, String version) {
        EditLogs revertLog = createLogEntry(username, project_name, version, EditType.REVERT);
        editLogsRepository.save(revertLog);
    }

    private EditLogs createLogEntry(String username, String project_name, String version, EditType editType) {
        LocalDateTime now = LocalDateTime.now();
        return EditLogs.builder()
                .username(username)
                .projectName(project_name)
                .version(version)
                .editType(editType)
                .date(now.toLocalDate().toString())
                .time(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .build();
    }

    public List<EditLogs> getLogsByName(String projectName) {
        return editLogsRepository.findAllByProjectName(projectName);
    }
}
