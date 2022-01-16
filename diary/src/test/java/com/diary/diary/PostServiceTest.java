package com.diary.diary;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.PostRepository;
import com.diary.diary.Service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostServiceTest {

    @Autowired
    PostService service;

    @Autowired
    PostRepository repository;

    @Test
    public void addPost(){
        String title = "Test";
        String content = "This is Test.";
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
        String title = "삭제용";
        String content = "삭제 테스트 중입니다.";
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
