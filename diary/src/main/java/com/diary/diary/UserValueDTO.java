package com.diary.diary;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserValueDTO {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @Builder //생성자
    public UserValueDTO(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

    //db에 삽입할 entity 객체를 만들어 리턴해줌
    public Users toEntity() {
        return Users.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

}
