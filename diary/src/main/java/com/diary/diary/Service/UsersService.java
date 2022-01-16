package com.diary.diary.Service;

import com.diary.diary.Entity.Users;
import com.diary.diary.Repository.UsersRepository;
import com.diary.diary.DTO.UserValueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor //생성자를 통한 의존성 주입을 자동으로 해준다 ==> final 키워드가 붙은 필드를 찾아서
public class UsersService {

   //final로 하면 컴파일 시점에 값이 들어가는지 확인할 수 있어서 좋다
   private final UsersRepository userrepo;

    /**
     * 회원가입
     * @param valueDTO 사용자 정보
     */
   public void join(UserValueDTO.UserRequestDto valueDTO) {

       //email 중복 검사
       if(checkEmail(valueDTO.getEmail())){

           //DB에 저장
           userrepo.save(valueDTO.toEntity());

       }
   }

    /**
     * 가입하려는 이메일의 중복 여부를 확인 하는 메소드
     * @param email 가입하려는 회원의 이메일
     * @return 이메일 존재 여부를 boolean으로 반환한다.
     */
   public boolean checkEmail(String email){

       //email 중복 검사
       return userrepo.existsByEmail(email);

   }


    /**
     * 회원 조회 메소드
     * @return 회원 정보를 가진 UserResponseDto 객체 list 반환
     */
   public List<UserValueDTO.UserResponseDto> selectAll(){
       List<UserValueDTO.UserResponseDto> list = new ArrayList<>();
       for (Users user: userrepo.findAll()) {
           list.add(new UserValueDTO.UserResponseDto(user));
       }
       return list;
   }

}
