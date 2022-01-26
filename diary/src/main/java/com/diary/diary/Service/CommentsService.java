package com.diary.diary.Service;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.Entity.Comments;
import com.diary.diary.Repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepo;

    //댓글 생성
    public void addComment(CommentValueDTO.CommentRequestDto requestDto){
        commentsRepo.save(requestDto.toEntity());
    }

    //댓글 삭제
    public void deleteComment(long id){
        commentsRepo.delete(commentsRepo.findById(id));
    }

    public List<CommentValueDTO.CommentResponseDto> selectAllComments(){
       List<CommentValueDTO.CommentResponseDto> list =  new ArrayList<>();

       for(Comments comment: commentsRepo.findAll()){
           list.add(new CommentValueDTO.CommentResponseDto(comment));
       }
       return list;
    }
}
