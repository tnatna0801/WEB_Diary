package com.diary.diary.Controller;



import com.diary.diary.DTO.UserValueDTO;
import com.diary.diary.Repository.UsersRepository;
import com.diary.diary.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
//    @PostMapping ("/join") join.html에서 form이 post 방식인데?? 왜 Request method 'GET' not supported 오류가 나지? 상관이 없나?
//    @JsonProperty(value = "UserValueDTO") //HTTP Request의 body 형식을 JSON으로 변경하여 Required request body is missing 오류 해결.. ??
    @PostMapping("/join")
    public String userJoin(@Validated UserValueDTO valueDTO){

        usersService.join(valueDTO);

        return "redirect:/join";
    }


}
