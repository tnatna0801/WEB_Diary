package com.diary.diary.Repository;


import com.diary.diary.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, String> {
    Comments findById(long id);
}
