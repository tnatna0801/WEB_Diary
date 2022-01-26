package com.diary.diary.Controller;

import com.diary.diary.Entity.Comments;
import com.diary.diary.Repository.CommentsRepository;
import com.diary.diary.Service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentsController {

    @Autowired
    private final CommentsService service;

    public CommentsController(CommentsService service){
        this.service = service;
    }

}
