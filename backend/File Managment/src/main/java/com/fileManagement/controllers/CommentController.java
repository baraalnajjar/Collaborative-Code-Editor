package com.fileManagement.controllers;
import com.fileManagement.entities.Project;
import com.fileManagement.repositories.ProjectRepository;
import com.fileManagement.services.commentServices.PostCommentService;
import com.fileManagement.services.commentServices.ReadCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;


@RestController
@RequestMapping("/comment")
public class CommentController {

    private final PostCommentService postCommentService;
    private final ReadCommentService readCommentService;
    private final ProjectRepository projectRepository;


    public CommentController(PostCommentService postCommentService,
                             ReadCommentService readCommentService,
                             ProjectRepository projectRepository) {
        this.postCommentService = postCommentService;
        this.readCommentService = readCommentService;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> postComment( @RequestParam Long userID,
                                                            @RequestBody Map<String, String> requestBody ) {
        String comment = requestBody.get("comment");
        long project_ID = Long.parseLong(requestBody.get("project_id"));
        Project project= projectRepository.findByid(project_ID);

        return postCommentService.postComment(userID,comment,project);
    }


    @GetMapping("/read")
    public ResponseEntity<Map<String, Object>> readComments(@RequestParam Long project_id) {
        return readCommentService.readComments(project_id);
        }



}

