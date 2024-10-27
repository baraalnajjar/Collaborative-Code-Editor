package com.fileManagement.repositories;

import com.fileManagement.entities.Comment;
import com.fileManagement.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT c.comment, u.first_name, u.last_name, c.date, c.time FROM app_comment " +
            "c JOIN app_user u ON c.user_id = u.id WHERE c.project_id = :projectId", nativeQuery = true)
    List<Object[]> findCommentsWithUsernamesByProject(@Param("projectId") Long projectId);


}
