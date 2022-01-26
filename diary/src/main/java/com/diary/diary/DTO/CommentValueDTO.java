package com.diary.diary.DTO;

import com.diary.diary.Entity.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public class CommentValueDTO {

    @Getter
    @Setter
    public static class CommentRequestDto{
        private long id;
        private String content;
        private Date createDate;
        private Date updateDate;
        private String privacy;

        @Builder
        public CommentRequestDto(String content,
                                 Date createDate,
                                 Date updateDate,
                                 String privacy){
            this.content = content;
            this.createDate = createDate;
            this.updateDate = updateDate;
            this.privacy = privacy;
        }

        public Comments toEntity(){
            return Comments.builder()
                    .content(content)
                    .createDate(createDate)
                    .updateDate(updateDate)
                    .privacy(privacy)
                    .build();
        }

    }

    @Getter
    @Setter
    public static class CommentResponseDto{
        private long id;
        private String content;
        private Date createDate;
        private Date updateDate;
        private String privacy;

        public CommentResponseDto(Comments comment){
            this.id = comment.getId();
            this.content = comment.getContent();
            this.createDate = comment.getCreateDate();
            this.updateDate = comment.getUpdateDate();
            this.privacy = comment.getPrivacy();
        }

    }
}
