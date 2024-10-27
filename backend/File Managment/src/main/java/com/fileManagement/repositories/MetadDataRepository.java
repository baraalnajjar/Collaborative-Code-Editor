package com.fileManagement.repositories;

import com.fileManagement.entities.MetaData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetadDataRepository extends MongoRepository<MetaData, String> {
    MetaData findByName(String projectName);

}

