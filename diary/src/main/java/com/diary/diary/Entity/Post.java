package com.diary.diary.Entity;


import com.diary.diary.DTO.PostValueDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;


@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@Getter
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Column(name = "img_url")
    private String imgUrl;

    private String weather;

    private String feeling;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Date updatedDate;

    private String color;

    @NotNull
    private String privacy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Builder
    public Post(String title, String content, String imgUrl,
                String weather, String feeling, Date createDate,
                Date updatedDate, String color, String privacy) {
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

    public void updateInfo(PostValueDTO.PostRequestDto requestDto) {

        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imgUrl = requestDto.getImgUrl();
        this.weather = requestDto.getWeather();
        this.feeling = requestDto.getFeeling();
        this.createDate = requestDto.getCreateDate();
        this.updatedDate = requestDto.getUpdatedDate();
        this.color = requestDto.getColor();
        this.privacy = requestDto.getPrivacy();

    }
}
