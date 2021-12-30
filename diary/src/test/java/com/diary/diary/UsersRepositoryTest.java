package com.diary.diary;



import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersRepositoryTest {
    @Autowired
    UsersRepository usersrepo;

    @Test
    public void getId() {
        String user_id="홍길동";
        String email="hong12@gmail.com";
        String password="gildong1234";

        usersrepo.save(Users.builder().user_id(user_id).email(email).password(password).build());

        Assertions.assertEquals("홍길동", usersrepo.getById("홍길동").getUser_id());

    }
}
