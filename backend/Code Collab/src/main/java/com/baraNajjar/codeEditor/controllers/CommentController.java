package com.baraNajjar.codeEditor.controllers;

import com.baraNajjar.codeEditor.services.CommentService;
import com.baraNajjar.codeEditor.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/read")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> fetchComments(@RequestParam Long project_id) {
        Map<String, Object> response = commentService.fetchComments(project_id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/post")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> postComment( @RequestParam Long userID,
                                                            @RequestBody Map<String, String> requestBody ) {
        return commentService.postComment(userID,requestBody);
    }
}
