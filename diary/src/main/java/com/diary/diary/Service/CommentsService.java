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
    public long addComment(CommentValueDTO.CommentRequestDto requestDto){
        return commentsRepo.save(requestDto.toEntity(postRepo.findById(requestDto.getPost_id()))).getId();
    }

    //댓글 수정
    public boolean updateComment(CommentValueDTO.CommentRequestDto requestDto) {
        Comments comment = commentsRepo.findById(requestDto.getId());
        comment.updateInfo(requestDto, comment.getPost());

        String updateComment = commentsRepo.save(comment).getContent();

        return updateComment.equals(requestDto.getContent());
    }



    //댓글 삭제
    public boolean deleteComment(long id){
        commentsRepo.delete(commentsRepo.findById(id));
        return !commentsRepo.existsById(id);
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
