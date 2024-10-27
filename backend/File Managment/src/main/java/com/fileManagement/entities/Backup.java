package com.fileManagement.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "backups")
public class Backup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String projectName;
    private String code;
    private String version;
}
