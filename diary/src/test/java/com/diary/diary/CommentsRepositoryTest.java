package com.diary.diary;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Entity.Comments;
import com.diary.diary.Repository.CommentsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentsRepositoryTest {

    @Autowired
    CommentsRepository repository;

    @Test
    public void findByIdTest(){
        String content = "댓글 작성 테스트";
        String privacy = "public";

        CommentValueDTO.CommentRequestDto requestDto = CommentValueDTO.CommentRequestDto.builder()
                .content(content)
                .privacy(privacy)
                .build();

        CommentValueDTO.CommentResponseDto comment = new CommentValueDTO.CommentResponseDto(repository.save(requestDto.toEntity()));
        CommentValueDTO.CommentResponseDto findedComment = new CommentValueDTO.CommentResponseDto(repository.findById(comment.getId()));

        Assertions.assertEquals(comment.getContent(), findedComment.getContent());

    }
}
