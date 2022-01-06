package com.diary.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //생성자를 통한 의존성 주입을 자동으로 해준다 ==> final 키워드가 붙응ㄴ 필드를 찾아서
public class UsersService {

   //final로 하면 컴파일 시점에 값이 들어가는지 확인할 수 있어서 좋다?
   private final UsersRepository userrepo;

    /**
     * 회원가입
     * @param valueDTO 사용자 정보
     * @return 사용자의 name
     */
   public String join(UserValueDTO valueDTO) {

       userrepo.save(valueDTO.toEntity());
       return valueDTO.getName();

   }

}