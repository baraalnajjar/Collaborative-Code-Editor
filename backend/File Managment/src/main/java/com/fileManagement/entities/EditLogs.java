package com.fileManagement.entities;

import com.fileManagement.enums.EditType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "app_editLogs")
public class EditLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EditType editType;

    @Column(name = "date", nullable = false)
    @Size(max = 100)
    private String date;

    @Column(name = "time", nullable = false)
    @Size(max = 100)
    private String time;

}