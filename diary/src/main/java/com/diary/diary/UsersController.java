package com.diary.diary;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class UsersController {

    private final UsersRepository repo;

    public UsersController(UsersRepository repo) {
        this.repo = repo;
    }

    /**
     * 사용자 조회
     * @return users.html 회원 가입 페이지
     */
    @GetMapping("/users")
    public String findAllUsers() {

        Users user = new Users();
        repo.save(user);
        return "users";
    }

//    @PostMapping("Users")
//    public Users signUp() {
//        final Users user = Users.builder();
//
//    }
}
