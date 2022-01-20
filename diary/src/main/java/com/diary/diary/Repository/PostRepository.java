package com.diary.diary.Repository;

import com.diary.diary.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {

    Post findById(long id);

    boolean existsById(long id);
}
