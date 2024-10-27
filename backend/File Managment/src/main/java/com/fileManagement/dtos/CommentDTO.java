package com.fileManagement.dtos;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private String comment;
    private String name;
    private String date;
    private String time;

}

