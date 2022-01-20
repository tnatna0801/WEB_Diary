package com.diary.diary.Controller;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @param Id 삭제할 일기의 id를 가지고 있는 객체
     * @return 일기 목록 조회
     */
    @RequestMapping("/deletePost")
    public String deletePost(long Id){
        postService.deletePost(Id);
        return "redirect:/postlist";
    }

}
