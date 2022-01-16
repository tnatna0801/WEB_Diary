package com.diary.diary;

import com.diary.diary.DTO.PostValueDTO;
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

        PostValueDTO.PostRequestDto requestDto = PostValueDTO.PostRequestDto.builder()
                .title(title)
                .content(content)
                .privacy(privacy)
                .build();

        Post savedPost = postRepo.save(requestDto.toEntity());

        Assertions.assertEquals(title, savedPost.getTitle());
    }

    @Test
    public void deletePost(){
        String title = "삭제용";
        String content = "삭제 테스트 중입니다.";
        String privacy = "public";

        PostValueDTO.PostRequestDto requestDto = PostValueDTO.PostRequestDto.builder()
                .title(title)
                .content(content)
                .privacy(privacy)
                .build();

        Post savedPost = postRepo.save(requestDto.toEntity());

        long id = savedPost.getId();

        postRepo.delete(savedPost);
        Assertions.assertNull(postRepo.findById(id));
    }



}
