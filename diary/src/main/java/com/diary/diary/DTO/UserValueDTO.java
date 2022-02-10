package com.diary.diary.DTO;

import com.diary.diary.Entity.Users;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;


public class UserValueDTO {

    @Getter
    @NoArgsConstructor
    public static class UserRequestDto{

        @NotNull
        private String email;

        @NotNull
        private String name;

        @NotNull
        private String password;

        @Builder //생성자
        public UserRequestDto(String email, String name, String password){
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


    @Getter
    @Setter
    public static class UserResponseDto{

        private long id;

        private String email;

        private String name;

        private String password;


        public UserResponseDto(Users user){
            this.id = user.getId();
            this.email = user.getEmail();
            this.name = user.getName();
            this.password = user.getPassword();
        }
    }
}
