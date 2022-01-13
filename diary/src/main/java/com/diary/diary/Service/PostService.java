package com.diary.diary.Service;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepo;

    public PostService(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    //일기 생성
    public void savePost(PostValueDTO.PostRequestDto PostRequest){
        postRepo.save(PostRequest.toEntity());
    }

    //일기 삭제
    public void deletePost(PostValueDTO.PostRequestDto PostRequest){
        //ID를 어떻게 가져오지?? response?
        //postRepo.delete(postRepo.findById());
    }

    //일기 리스트 조회
    public List<Post> allPosts(){
        return postRepo.findAll();
    }
}
