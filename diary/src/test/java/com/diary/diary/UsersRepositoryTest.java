package com.diary.diary;



import com.diary.diary.DTO.UserValueDTO;
import com.diary.diary.Entity.Users;
import com.diary.diary.Repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersRepositoryTest {
    @Autowired
    UsersRepository usersrepo;
    
    @BeforeEach
    public void clear(){
        usersrepo.deleteAll();
    }
    
    @Test
    public void getName() {
        String name="홍길동";
        String email="hong12@gmail.com";
        String password="gildong1234";

        UserValueDTO.UserRequestDto requestDto = UserValueDTO.UserRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();


        Users saveduser = usersrepo.save(requestDto.toEntity());
        Assertions.assertEquals(requestDto.getName(), saveduser.getName());

    }

    @Test
    public void duplicateEmail(){
        String name="김진수";
        String email="soo@gmail.com";
        String password="asdf123";

        UserValueDTO.UserRequestDto requestDto = UserValueDTO.UserRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();


        Users savedUser = usersrepo.save(requestDto.toEntity());

        Assertions.assertTrue(usersrepo.existsByEmail(savedUser.getEmail()));
    }
}
