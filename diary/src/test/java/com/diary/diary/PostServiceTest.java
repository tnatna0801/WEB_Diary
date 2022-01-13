package com.diary.diary;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostServiceTest {

    @Autowired
    PostService service;

    @Test
    public void writePost(){
        //service.savePost();
    }

    @Test
    public void deletePost(){
//        PostValueDTO.PostResponseDto responseDto;
//
//        service.deletePost(responseDto.getId());
    }
}
