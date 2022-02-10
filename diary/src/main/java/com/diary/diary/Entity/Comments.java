package com.diary.diary.Entity;

import com.diary.diary.DTO.CommentValueDTO;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name="comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String content;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Date updateDate;

    @NotNull
    private String privacy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comments(String content, Date createDate,
                    Date updateDate, String privacy,
                    Post post){
        this.content = content;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.privacy = privacy;
        this.post = post;
    }

    public void updateInfo(CommentValueDTO.CommentRequestDto requestDto, Post post) {
        this.content = requestDto.getContent();
        this.createDate = requestDto.getCreateDate();
        this.updateDate = requestDto.getUpdateDate();
        this.privacy = requestDto.getPrivacy();
        this.post = post;

    }
}
