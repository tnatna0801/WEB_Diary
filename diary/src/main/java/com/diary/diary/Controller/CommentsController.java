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

//    /**
//     * 댓글 저장 메소드
//     * @param  댓글과 공개여부를 담은 객체
//     * @return 다시 이릭 상세 페이지로 가야하지만
//     * 어떻게 post id를 가져와야할지 몰라서 임시로 postlist 페이지를 반환하도록함
//     */

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
    public boolean addComment(long post_id, String content, String privacy){
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



}
