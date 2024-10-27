package com.fileManagement.services.commentServices;

import com.fileManagement.dtos.CommentDTO;
import com.fileManagement.repositories.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReadCommentService {

    private final CommentRepository commentRepository;

    public ReadCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public ResponseEntity<Map<String, Object>> readComments(long project_id) {
        List<Object[]> results = commentRepository.findCommentsWithUsernamesByProject(project_id);

        List<CommentDTO> commentList = new ArrayList<>();

        for (Object[] result : results) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment((String) result[0]); // comment
            commentDTO.setName((String) (result[1] + " " + result[2]));  // Full name
            commentDTO.setDate((String) result[3]);  // Date
            commentDTO.setTime((String) result[4]);  // Time
            commentList.add(commentDTO);
        }

        Map<String, Object> response = new HashMap<>();
        if (!commentList.isEmpty()) {
            response.put("message", "Comments found successfully.");
            response.put("comments", commentList);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "No comments found for the project: " + project_id);
            return ResponseEntity.ok(response);
        }
    }

}
