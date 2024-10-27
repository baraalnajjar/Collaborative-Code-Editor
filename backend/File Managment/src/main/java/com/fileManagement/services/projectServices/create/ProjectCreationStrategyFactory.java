package com.fileManagement.services.projectServices.create;



import com.fileManagement.repositories.MetadDataRepository;
import com.fileManagement.repositories.ProjectRepository;
import com.fileManagement.services.projectServices.create.impl.JavaProjectCreationStrategy;
import com.fileManagement.services.projectServices.create.impl.PythonProjectCreationStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProjectCreationStrategyFactory {

    private final Map<String, ProjectCreationStrategy> strategies = new HashMap<>();


    public ProjectCreationStrategyFactory(ProjectRepository projectRepository,MetadDataRepository metadDataRepository) {
        strategies.put("java", new JavaProjectCreationStrategy(projectRepository,metadDataRepository));
        strategies.put("python", new PythonProjectCreationStrategy(projectRepository,metadDataRepository));
    }

    public ProjectCreationStrategy getStrategy(String language) {
        String cleanedLanguage = language != null ? language.trim().replace("\"", "").toLowerCase() : null;
        return strategies.get(cleanedLanguage);
    }

}
