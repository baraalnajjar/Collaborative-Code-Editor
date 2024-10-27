package com.fileManagement.entities;

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
@Table(name = "app_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date", nullable = false)
    @Size(max = 100)
    private String date;

    @Column(name = "time", nullable = false)
    @Size(max = 100)
    private String time;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
