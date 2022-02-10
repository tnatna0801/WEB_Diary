package com.diary.diary.Controller;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.Entity.Comments;
import com.diary.diary.Repository.CommentsRepository;
import com.diary.diary.Service.CommentsService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentsController {

    @Autowired
    private final CommentsService service;

    public CommentsController(CommentsService service){
        this.service = service;
    }

    /**
     * 댓글 저장 메소드
     * @param post_id 포스트의 id
     * @param content 댓글 내용
     * @param privacy 댓글 공개 여부
     * @return 저장이 되었다면 true 아니라면 false를 반환한다.
     */
    @PostMapping("/addcomment")
    @JsonProperty
    @ResponseBody
    public long addComment(long post_id, String content, String privacy){
        return service.addComment(CommentValueDTO.CommentRequestDto.builder()
                .content(content)
                .privacy(privacy)
                .post_id(post_id).build());
    }

    @RequestMapping("/comment/list")
    @JsonProperty
    @ResponseBody
    public List<CommentValueDTO.CommentResponseDto> commentList(long post_id){
        return service.selectAllComments(post_id);
    }

    @PostMapping("/comment/delete")
    @JsonProperty
    @ResponseBody
    public boolean deleteComment(long id){
        return service.deleteComment(id);
    }

    @PostMapping("/comment/update")
    @JsonProperty
    @ResponseBody
    public boolean updateComment(CommentValueDTO.CommentRequestDto requestDto){
        return service.updateComment(requestDto);
    }


}
