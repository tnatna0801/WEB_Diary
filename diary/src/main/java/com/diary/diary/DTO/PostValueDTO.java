package com.diary.diary.DTO;

import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.PostRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

public class PostValueDTO {

    @Getter
    @Setter
    public static class PostRequestDto{

        private String title;

        private String content;

        private String imgUrl;

        private String weather;

        private String feeling;

        private Date createDate;

        private Date updatedDate;

        private Date deletedDate;

        private String color;

        private String privacy;

        public PostRequestDto(String title, String content, String imgUrl,
                                            String weather, String feeling, Date createDate,
                                            Date updatedDate, Date deletedDate,
                                            String color, String privacy) {
            this.title = title;
            this.content = content;
            this.imgUrl = imgUrl;
            this.weather = weather;
            this.feeling = feeling;
            this.createDate = createDate;
            this.updatedDate = updatedDate;
            this.deletedDate = deletedDate;
            this.color = color;
            this.privacy = privacy;
        }

        public Post toEntity(){
            return Post.builder()
                    .title(title)
                    .content(content)
                    .imgUrl(imgUrl)
                    .weather(weather)
                    .feeling(feeling)
                    .createDate(createDate)
                    .updatedDate(updatedDate)
                    .deletedDate(deletedDate)
                    .color(color)
                    .privacy(privacy)
                    .build();

        }

    }

//    //
//    public Post postResponseDto(){
//        PostRepository postRepo;
//        return postRepo.getById();
//    }

}
