package com.diary.diary.Controller;

import com.diary.diary.DTO.CommentValueDTO;
import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Service.CommentsService;
import com.diary.diary.Service.PostService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class PostsController {

    @Autowired
    private final PostService postService;
    private final CommentsService commentService;

    public PostsController(PostService postService, CommentsService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    /**
     * 일기 작성 화면
     * @return 작성 화면
     */
    @GetMapping("/writepost")
    public String writePost(){
        return "writepost";
    }

    /**
     * 작성한 일기 저장
     * @param PostRequestDto 작성된 일기 데이터를 가지고 있는 객체
     * @return 작성한 일기를 확인할 수 있는 페이지
     */
    @PostMapping("/writepost")
    public String savePost(PostValueDTO.PostRequestDto PostRequestDto){
        postService.savePost(PostRequestDto);
        return "postlist";}

    /**
     * 일기 상세 페이지
     * @param id 일기 ID
     * @return 일기 상세 페이지
     */
    @RequestMapping("/post/{id}")
    public String detailPost(@PathVariable long id, Model model) {
        model.addAttribute("post", postService.selectPost(id));
        return "post";
    }

    /**
     * 댓글 저장 메소드
     * @param requestDto 댓글과 공개여부를 담은 객체
     * @return 다시 이릭 상세 페이지로 가야하지만
     * 어떻게 post id를 가져와야할지 몰라서 임시로 postlist 페이지를 반환하도록함
     */
    @PostMapping("/addcomment")
    public String addComment(CommentValueDTO.CommentRequestDto requestDto, @RequestParam("post_id") long post_id){
        commentService.addComment(requestDto);
//        return "post/{post_id}";
        return "postlist";
    }


    /**
     * 일기 목록 조회
     * @return postlist.html 일기 조회 페이지
     */
    @RequestMapping("/postlist")
    public String postList(Model model) {
        model.addAttribute("postList", postService.allPosts());
        return "postlist";
    }


    /**
     * 일기 삭제
     * @param id 삭제할 일기의 Id
     * @return 삭제가 되었는지 결과를 boolean으로 반환
     */
    @RequestMapping("/deletePost")
    @JsonProperty
    @ResponseBody
    public boolean deletePost(long id){
        return postService.deletePost(id);
    }

    /**
     * 수정한 일기 업데이트
     * @param PostRequestDto 수정된 일기 데이터를 가진 객체
     * @return 다시 일기 상세페이지를 반환하고 싶은데 방법을 못찾아서 임시로 index 페이지를 반환함
     */
    @PostMapping("/updatePost")
    public String updatePost(PostValueDTO.PostRequestDto PostRequestDto){
        postService.updatePost(PostRequestDto);
        return "index";
    }

    /**
     * 일기 수정 페이지
     * @param id 수정하려는 일기의 id 값
     * @param model
     * @return 일기수정페이지를 반환
     */
    @RequestMapping("/editPost/{id}")
    public String editPostForm(@PathVariable long id, Model model) {
        model.addAttribute("post", postService.selectPost(id));
        return "editPost";
    }

}
