package com.diary.diary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

//    @GetMapping("/") // 루트 경로인 /의 웹 요청을 처리한다.
//    public String home() {
//        return "home";
//    }

    @RequestMapping("/dashboard")
    public String dashboard(){
        return "index";
    }
}
