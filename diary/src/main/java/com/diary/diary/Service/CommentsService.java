package com.diary.diary.Service;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.Entity.Comments;
import com.diary.diary.Repository.CommentsRepository;
import com.diary.diary.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepo;
    private final PostRepository postRepo;

    //댓글 생성
    public boolean addComment(CommentValueDTO.CommentRequestDto requestDto){
        long id = commentsRepo.save(requestDto.toEntity(postRepo.findById(requestDto.getPost_id()))).getId();

        return id >= 1;
    }

    //댓글 삭제
    public void deleteComment(long id){
        commentsRepo.delete(commentsRepo.findById(id));
    }

    //전체 댓글 조회
    public List<CommentValueDTO.CommentResponseDto> selectAllComments(long postId){
       List<CommentValueDTO.CommentResponseDto> list =  new ArrayList<>();

       for(Comments comment: commentsRepo.findByPostId(postId)){
               System.out.println(comment.getPost().getId());
               list.add(new CommentValueDTO.CommentResponseDto(comment));
       }
       return list;
    }


}
