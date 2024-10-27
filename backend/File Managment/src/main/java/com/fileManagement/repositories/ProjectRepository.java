package com.fileManagement.repositories;

import com.fileManagement.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProjectRepository  extends JpaRepository<Project, Long> {
    Project findByid(Long id);
    Project findByName(String fileName);
    List<Project> findByLanguage(String language);

}
