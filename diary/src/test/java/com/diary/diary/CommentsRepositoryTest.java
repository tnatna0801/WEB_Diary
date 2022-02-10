package com.diary.diary;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Entity.Comments;
import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.CommentsRepository;
import com.diary.diary.Repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentsRepositoryTest {

    @Autowired
    private CommentsRepository repository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void findByIdTest(){

        String title = "삭제용";
        String content = "삭제 테스트 중입니다.";
        String privacy = "public";

        PostValueDTO.PostRequestDto postRequest = PostValueDTO.PostRequestDto.builder()
                .title(title)
                .content(content)
                .privacy(privacy)
                .build();

        Post savedPost = postRepository.save(postRequest.toEntity());

        CommentValueDTO.CommentRequestDto commentRequest = CommentValueDTO.CommentRequestDto.builder()
                .content("댓글 작성 테스트")
                .privacy("public")
                .build();

        CommentValueDTO.CommentResponseDto comment = new CommentValueDTO.CommentResponseDto(repository.save(commentRequest.toEntity(savedPost)));
        CommentValueDTO.CommentResponseDto findComment = new CommentValueDTO.CommentResponseDto(repository.findById(comment.getId()));

        Assertions.assertEquals(comment.getContent(), findComment.getContent());

    }

    @Test
    public void existByIdTest(){

    }
}
