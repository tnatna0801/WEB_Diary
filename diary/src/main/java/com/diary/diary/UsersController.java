package com.diary.diary;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UsersController {

    private final UsersRepository repo;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersRepository repo, UsersService usersService){
        this.repo = repo;
        this.usersService = usersService;
    }

    /**
     * 회원 조회
     * @return users.html 회원 조회 페이지
     */
    @RequestMapping("/users")
    public String allUsers(Model model) {

        List<Users> userList = repo.findAll();
        model.addAttribute("userList", userList);
        return "users";
    }

    /**
     * 회원가입
     * @param valueDTO user 값을 담을 객체
     * @return join.html 회원가입 페이지
     */
    @PostMapping ("/join")
//    @JsonProperty(value = "UserValueDTO") //HTTP Request의 body 형식을 JSON으로 변경하여 Required request body is missing 오류 해결.. ??
    public String userJoin(UserValueDTO valueDTO){

        usersService.join(valueDTO);

        return "join";
    }
}
