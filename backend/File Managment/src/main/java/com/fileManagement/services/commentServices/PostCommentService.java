package com.fileManagement.services.commentServices;

import com.fileManagement.entities.Comment;
import com.fileManagement.entities.Project;
import com.fileManagement.repositories.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostCommentService {


    private final CommentRepository commentRepository;

    public PostCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public ResponseEntity<Map<String, Object>> postComment(Long user_ID, String commentText, Project project) {
        Comment comment = Comment.builder()
                .comment(commentText)
                .project(project)
                .date(LocalDate.now().toString())
                .time(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .userID(user_ID)
                .build();
        commentRepository.save(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comment posted successfully.");
        return ResponseEntity.ok(response);
    }
}
