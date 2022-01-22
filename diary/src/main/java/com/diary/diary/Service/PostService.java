package com.diary.diary.Service;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.DTO.UserValueDTO;
import com.diary.diary.Entity.Post;
import com.diary.diary.Entity.Users;
import com.diary.diary.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public boolean deletePost(long Id){

        postRepo.delete(postRepo.findById(Id));
        return postRepo.existsById(Id);
    }

    //일기 리스트 조회
    public List<PostValueDTO.PostResponseDto> allPosts(){
        List<PostValueDTO.PostResponseDto> list = new ArrayList<>();
        for(Post post: postRepo.findAll()){
            list.add(new PostValueDTO.PostResponseDto(post));
        }

        return list;
    }

    public PostValueDTO.PostResponseDto selectPost(long Id){
        return new PostValueDTO.PostResponseDto(postRepo.findById(Id));
    }
}
