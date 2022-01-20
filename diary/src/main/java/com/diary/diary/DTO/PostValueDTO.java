package com.diary.diary.DTO;

import com.diary.diary.Entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

public class PostValueDTO {

    @Getter
    @Setter
    public static class PostRequestDto{

        private long id;

        private String title;

        private String content;

        private String imgUrl;

        private String weather;

        private String feeling;

        private Date createDate;

        private Date updatedDate;

        private String color;

        private String privacy;

        @Builder
        public PostRequestDto(String title, String content, String imgUrl,
                                            String weather, String feeling,
                                            Date createDate, Date updatedDate,
                                            String color, String privacy) {
            this.title = title;
            this.content = content;
            this.imgUrl = imgUrl;
            this.weather = weather;
            this.feeling = feeling;
            this.createDate = createDate;
            this.updatedDate = updatedDate;
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
                    .color(color)
                    .privacy(privacy)
                    .build();

        }

    }

    @Getter
    public static class PostResponseDto{
        private final long id;

        private final String title;

        private final String content;

        private final String imgUrl;

        private final String weather;

        private final String feeling;

        private final Date createDate;

        private final Date updatedDate;

        private final String color;

        private final String privacy;

        public PostResponseDto(Post post){
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.imgUrl = post.getImgUrl();
            this.weather = post.getWeather();
            this.feeling = post.getFeeling();
            this.createDate = post.getCreateDate();
            this.updatedDate = post.getUpdatedDate();
            this.color = post.getColor();
            this.privacy = post.getPrivacy();

        }
    }

}
