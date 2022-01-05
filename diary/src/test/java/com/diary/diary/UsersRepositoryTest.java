package com.diary.diary;



import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
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

        Users user = new Users(email, name, password);

        Users saveduser = usersrepo.save(user);
        Assertions.assertEquals(user.getName(), saveduser.getName());

    }
}
