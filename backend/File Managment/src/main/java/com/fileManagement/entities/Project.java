package com.fileManagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "app_project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "creation_time", nullable = false)
    private String creationTime;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "current_version", nullable = false)
    private String version;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectParticipant> participants;
}
