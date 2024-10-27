package com.fileManagement.services.projectServices.create.impl;

import com.fileManagement.entities.MetaData;
import com.fileManagement.entities.Project;
import com.fileManagement.repositories.MetadDataRepository;
import com.fileManagement.repositories.ProjectRepository;
import com.fileManagement.services.projectServices.create.ProjectCreationStrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PythonProjectCreationStrategy implements ProjectCreationStrategy {


    private final ProjectRepository projectRepository;
    private final MetadDataRepository metadDataRepository;

    public PythonProjectCreationStrategy(ProjectRepository projectRepository, MetadDataRepository metadDataRepository) {
        this.projectRepository = projectRepository;
        this.metadDataRepository = metadDataRepository;
    }

    @Override
    public String createProject(String projectName,String username) {

        String code= "def main():\n" +
                "    print('Hello, World!')\n" +
                "\n" +
                "if __name__ == '__main__':\n" +
                "    main()";

        Project project = Project.builder()
                .name(projectName)
                .language("Python")
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