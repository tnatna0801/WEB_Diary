package com.diary.diary.Controller;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.Entity.Comments;
import com.diary.diary.Repository.CommentsRepository;
import com.diary.diary.Service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentsController {

    @Autowired
    private final CommentsService service;

    public CommentsController(CommentsService service){
        this.service = service;
    }

    /**
     * 댓글 저장 메소드
     * @param requestDto 댓글과 공개여부를 담은 객체
     * @return 다시 이릭 상세 페이지로 가야하지만
     * 어떻게 post id를 가져와야할지 몰라서 임시로 postlist 페이지를 반환하도록함
     */
    @PostMapping("/addcomment")
    public String addComment(CommentValueDTO.CommentRequestDto requestDto){
        service.addComment(requestDto);
        return "postlist";
    }

}
