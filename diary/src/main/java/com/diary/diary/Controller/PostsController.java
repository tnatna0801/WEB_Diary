package com.diary.diary.Controller;

import com.diary.diary.DTO.PostValueDTO;
import com.diary.diary.Service.PostService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.UUID;


@Controller
public class PostsController {

    @Autowired
    private final PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 일기 작성 화면
     * @return 작성 화면
     */
    @GetMapping("/writepost")
    public String writePost(){
        return "writepost";
    }

    /**
     * 작성한 일기 저장
     * @param PostRequestDto 작성된 일기 데이터를 가지고 있는 객체
     * @return 작성한 일기를 확인할 수 있는 페이지
     */
    @PostMapping("/writepost")
    public String savePost(PostValueDTO.PostRequestDto PostRequestDto){
        String detailPostUrl = "/post/" + postService.savePost(PostRequestDto).getId();
        return "redirect:"+detailPostUrl;}



    String filePath = "/Users/LG/Desktop/[WEB]Diary/diary_img/";
    /**
     * 이미지 파일 저장
     * @return 이미지가 저장된 url
     */
    @PostMapping("/imageUpload")
    public String imageUpload(MultipartFile multipartFile) throws IOException {

        //파일로 변환, 저장
        //service로 옮겨야함

        // 파일을 저장소에 저장하는 코드
        String fullFilename = multipartFile.getOriginalFilename();
        int lastIndex = fullFilename.lastIndexOf(".");
        String fileName = fullFilename.substring(0, lastIndex);
        String extension = fullFilename.substring(lastIndex + 1);

        //파일명 중복을 막기 위해서 UUID를 이용해 랜덤으로 image 파일명을 생성
        String newName = UUID.randomUUID() + "." + extension;
        String uploadPath = filePath + newName;

        //path 객체를 이용해 저장
        multipartFile.transferTo(Paths.get(uploadPath));

        System.out.println(Paths.get(uploadPath));


        return uploadPath; // 저장한 url return
    }

    /**
     * 이미지를 다운로드하여 뷰에 넘기기
     * @param filename 이미지 저장명
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/imageUpload/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + filePath + filename);
    }


    /**
     * 일기 상세 페이지
     * @param id 일기 ID
     * @return 일기 상세 페이지
     */
    @RequestMapping("/post/{id}")
    public String detailPost(@PathVariable long id, Model model) {
        model.addAttribute("post", postService.selectPost(id));
        return "post";
    }


    /**
     * 일기 목록 조회
     * @return postlist.html 일기 조회 페이지
     */
    @RequestMapping("/postlist")
    public String postList(Model model) {
        model.addAttribute("postList", postService.allPosts());
        return "postlist";
    }


    /**
     * 일기 삭제
     * @param id 삭제할 일기의 Id
     * @return 삭제가 되었는지 결과를 boolean으로 반환
     */
    @RequestMapping("/deletePost")
    @JsonProperty
    @ResponseBody
    public boolean deletePost(long id){
        return postService.deletePost(id);
    }

    /**
     * 수정한 일기 업데이트
     * @param PostRequestDto 수정된 일기 데이터를 가진 객체
     * @return 다시 일기 상세페이지를 반환
     */
    @PostMapping("/updatePost")
    public String updatePost(PostValueDTO.PostRequestDto PostRequestDto){
        postService.updatePost(PostRequestDto);
        String detailPost = "/post/" + PostRequestDto.getId();
        return "redirect:"+detailPost;
    }

    /**
     * 일기 수정 페이지
     * @param id 수정하려는 일기의 id 값
     * @param model 수정하려는 Post 객체를 뷰에 보내기 위한 model 객체
     * @return 일기수정페이지를 반환
     */
    @RequestMapping("/editPost/{id}")
    public String editPostForm(@PathVariable long id, Model model) {
        model.addAttribute("post", postService.selectPost(id));
        return "editPost";
    }

}
