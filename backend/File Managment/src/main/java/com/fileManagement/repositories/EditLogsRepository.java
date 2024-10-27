package com.fileManagement.repositories;

import com.fileManagement.entities.EditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface EditLogsRepository extends JpaRepository<EditLogs, Long> {
    List<EditLogs> findAllByProjectName(String project_name);
}

