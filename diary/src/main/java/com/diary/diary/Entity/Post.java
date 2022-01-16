package com.diary.diary.Entity;


import com.sun.istack.NotNull;
import lombok.*;

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
    private Date createDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "deleted_date")
    private Date deletedDate;

    private String color;

    @NotNull
    private String privacy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Builder
    public Post(String title, String content, String imgUrl,
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
}
