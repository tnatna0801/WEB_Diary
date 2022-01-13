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

    @GetMapping("/writepost")
    public String writePost(){
        return "writepost";
    }

    @PostMapping("/writepost")
    public String savePost(PostValueDTO.PostRequestDto PostRequestDto){
        postService.savePost(PostRequestDto);
        return "redirect:/writepost";}

    /**
     * 일기 목록 조회
     * @return postlist.html 회원 조회 페이지
     */
    @RequestMapping("/postlist")
    public String postList(Model model) {
        model.addAttribute("postList", postService.allPosts());
        return "postlist";
    }

}
