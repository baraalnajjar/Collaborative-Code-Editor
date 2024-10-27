package com.fileManagement.repositories;


import com.fileManagement.entities.Backup;
import com.fileManagement.entities.MetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupRepository extends MongoRepository<Backup, String> {

    List<Backup> findByProjectName(String projectName);
    Backup findByProjectNameAndVersion(String projectName, String version);


}
