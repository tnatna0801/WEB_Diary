package com.diary.diary;

import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepo;

    @Test
    public void savePost(){
        String title = "Test";
        String content = "This is Test.";
        String privacy = "public";

        Post post = new Post(title, content, privacy);

        Post savedPost = postRepo.save(post);

        Assertions.assertEquals(title, savedPost.getTitle());
    }

    public void deletePost(){

        //postRepo.delete();
    }

    public void editPost(){

    }


}
