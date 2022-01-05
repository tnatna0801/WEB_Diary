package com.diary.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {


}
