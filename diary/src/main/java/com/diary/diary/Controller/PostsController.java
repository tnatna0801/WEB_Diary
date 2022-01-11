package com.diary.diary.Controller;

import com.diary.diary.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostsController {

    @Autowired
    private final PostRepository postRepo;

    public PostsController(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    @GetMapping("/savepost")
    public String savePostForm(){
        return "savepost";
    }

    @PostMapping("/savepost")
    public String savePost(){
        return "savepost";
    }
}
