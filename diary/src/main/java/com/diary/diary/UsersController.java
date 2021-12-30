package com.diary.diary;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class UsersController {
    @Autowired
    private UsersRepository repo;

    /**
     * 사용자 조회
     * @return
     */
    @GetMapping("/users")
    public List<Users> findAllUsers() {
        return repo.findAll();
    }

//    @PostMapping("Users")
//    public Users signUp() {
//        final Users user = Users.builder();
//
//    }
}
