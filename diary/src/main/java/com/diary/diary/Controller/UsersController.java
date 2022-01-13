package com.diary.diary.Controller;



import com.diary.diary.DTO.UserValueDTO;
import com.diary.diary.Repository.UsersRepository;
import com.diary.diary.Service.UsersService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersRepository repo, UsersService usersService){
        this.usersService = usersService;
    }

    /**
     * 회원 조회
     * @return users.html 회원 조회 페이지
     */
    @RequestMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("userList", usersService.selectAll());
        return "users";
    }

    
    
    /**
     * 
     * @return join.html 회원가입 페이지
     */
    @GetMapping("/join")
    public String userJoinForm(){

        return "join";
    }


    /**
     * 회원가입
     * @param valueDTO user 값을 담을 객체
     * @return join.html 회원가입 완료 페이지 혹은 리다이렉트
     */
    @PostMapping("/join")
    public String userJoin(@Validated UserValueDTO valueDTO){

        usersService.join(valueDTO);

        return "redirect:/join";
    }

    @PostMapping("/check")
    @JsonProperty
    @ResponseBody
    public boolean checkUserEmail(String email){

        return usersService.checkEmail(email);
    }


}
