package com.diary.diary.Repository;

import com.diary.diary.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    boolean existsByEmail(String email);
}
