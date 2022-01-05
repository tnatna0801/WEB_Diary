package com.diary.diary;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Getter //Entity 클래스에서는 절대 Setter 메소드를 만들지 않는다.
@Entity
@NoArgsConstructor //기본 생성자 자동 추가?
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //데이터베이스에 기본키 생성을 위임
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @Builder
    public Users(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

}
