package com.diary.diary;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.PostRepository;
import com.diary.diary.Service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService service;

    @Autowired
    PostRepository repository;

    @Test
    public void addPost(){
        String title = "Test-service";
        String content = "This is Test. Service";
        String privacy = "public";

        PostValueDTO.PostRequestDto requestDto = PostValueDTO.PostRequestDto.builder()
                .title(title)
                .content(content)
                .privacy(privacy)
                .build();

        Post savedPost = service.savePost(requestDto);

        Assertions.assertEquals(requestDto.getTitle(), savedPost.getTitle());
    }

    @Test
    public void deletePost(){
        String title = "삭제용-service";
        String content = "삭제 테스트 중입니다. service";
        String privacy = "public";

        PostValueDTO.PostRequestDto requestDto = PostValueDTO.PostRequestDto.builder()
                .title(title)
                .content(content)
                .privacy(privacy)
                .build();

        Post savedPost = service.savePost(requestDto);

        long id = savedPost.getId();

        service.deletePost(id);

        Assertions.assertNull(repository.findById(id));
    }
}
