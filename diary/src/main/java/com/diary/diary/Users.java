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
@Builder
public class Users {

    @Id
    @GeneratedValue
    private String user_id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Builder
    public Users(String user_id, String email, String password) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
    }

}
