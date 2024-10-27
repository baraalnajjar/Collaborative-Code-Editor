package com.fileManagement.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "projects_codes")
public class MetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Indexed(unique = true)
    private String name;
    private String code;
    private String version;

}

