package com.diary.diary;



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

        Users user = new Users(email, name, password);

        Users saveduser = usersrepo.save(user);
        Assertions.assertEquals(user.getName(), saveduser.getName());

    }

    @Test
    public void duplicateEmail(){
        String name="김철수";
        String email="soo@gmail.com";
        String password="asdf123";

        Users user = new Users(email, name, password);

        Users savedUser = usersrepo.save(user);

        Assertions.assertTrue(usersrepo.existsByEmail(savedUser.getEmail()));
        System.out.println(usersrepo.existsByEmail(savedUser.getEmail()));
    }
}
