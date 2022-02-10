package com.diary.diary.Repository;


import com.diary.diary.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, String> {
    Comments findById(long id);
    List<Comments> findByPostId(long post_id);
    boolean existsById(long id);
}
