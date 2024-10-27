package com.fileManagement.services.projectServices.create.impl;

import com.fileManagement.entities.MetaData;
import com.fileManagement.entities.Project;
import com.fileManagement.repositories.MetadDataRepository;
import com.fileManagement.repositories.ProjectRepository;
import com.fileManagement.services.projectServices.create.ProjectCreationStrategy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class JavaProjectCreationStrategy implements ProjectCreationStrategy {

    private final ProjectRepository projectRepository;
    private final MetadDataRepository metadDataRepository;

    public JavaProjectCreationStrategy(ProjectRepository projectRepository, MetadDataRepository metadDataRepository) {
        this.projectRepository = projectRepository;
        this.metadDataRepository = metadDataRepository;
    }

    @Override
    public String createProject(String projectName, String username) {

        projectName = projectName.replace(" ", "");
        String code = "public class " + projectName + " {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}";
        Project project = Project.builder()
                .name(projectName)
                .language("Java")
                .creationDate(LocalDate.now())
                .creationTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .creator(username)
                .version("1.0")
                .build();

        projectRepository.save(project);

        MetaData metaData = MetaData.builder()
                .name(projectName)
                .code(code)
                .version("1.0")
                .build();

        metadDataRepository.save(metaData);

        return "Code Creation Was Successful";
    }
}

