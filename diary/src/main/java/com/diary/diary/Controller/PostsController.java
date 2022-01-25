package com.diary.diary.Controller;

import com.diary.diary.DTO.PostValueDTO;
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

    public PostsController(PostService postService) {
        this.postService = postService;
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

}
