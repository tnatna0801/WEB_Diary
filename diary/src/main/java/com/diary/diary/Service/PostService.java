package com.diary.diary.Service;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Entity.Post;
import com.diary.diary.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepo;

    //일기 생성
    public Post savePost(PostValueDTO.PostRequestDto request){
        return postRepo.save(request.toEntity());
    }

    //일기 삭제
    public void deletePost(long Id){

        postRepo.delete(postRepo.findById(Id));
    }

    //일기 리스트 조회
    public List<Post> allPosts(){
        return postRepo.findAll();
    }
}
