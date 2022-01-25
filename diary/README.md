## Step 6

#### 회원 가입

**이메일 중복 버튼을 누르면 이메일 중복 확인을 하고 결과를 알림창으로 알려준다. (ajax)**

오류 및 해결

* Uncaught ReferenceError: $ is not defined 발생 => jQuery가 로드되지 않아서 발생함
  * 해결하고자 아래 코드를 삽입하여 jQuery를 로드

```html
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
```
 
* Uncaught RangeError: Maximum call stack size exceeded
  * 무한루프때문에 발생한 에러라는 글이 많았으나 서버에 값을 넘기는 곳에 변수의 값이 아닌 input 태그자체를 넣었기때문에 문제가 되었다는 글을 발견하고 코드를 수정함
```javascript
변경 전
let email = document.getElementById("email");

변경 후
let email = $("#email").val()
```

* Uncaught ReferenceError: Alert is not defined
  *  브라우저에서 JavaScript 파일을 실행해야 한다는 글을 보았다. 
  *  alert였다...


#### Test

**PostServiceTest에서 post를 저장, 삭제하는 test 수행**

```java

Post savedPost = service.savePost(requestDto);

```

* NullPointerException 오류가 발생
 * 찾아보니 @Autowired 한 class가 null로 나와서 문제가 생긴 것 같다. 의존성 주입의 문제
 * PostServiceTest에 @SpringBootTest 안써서 생긴 문제였다. (해결) 


## Step 7

### 이미지 경로

**외부 폴더를 추가해 실제 시스템의 폴더를 사용**

왜냐하면 스프링 기본 폴더인 Resource/static 폴더를 사용하여 이미지를 업로드하게 되면, 해당 폴더에 이미지를 업로드 해도 실시간으로 확인되지 않고 서버 재실행 시 확인이 가능하기 때문

**DB에 파일 명만 저장해서 관리**

* 전체 경로를 다 저장하는 것은 후에 저장 경로가 바뀌는 경우가 생길 수도있어서 비추
* 여러 사진을 업로드한다면 파일 테이블을 따로 만들어야할 것 같다. 

---

외부폴더에 이미지가 업로드 된다면 용량은 어떻게 감당하지라는 의문이 들어서 클라우드나 서버를 사용하는 예제도 검색

**AWS S3 : 스토리지**

저장용량이 무한대이고 파일저장에 최적화되어있다. 
* 개념: https://ryan-han.com/post/aws/s3/
* Spring 연동 예제: https://devlog-wjdrbs96.tistory.com/323

* 비용? 5GB 무료
 * AWS 신규 가입 고객은 1년 동안 매달 5GB의 Amazon S3 스토리지(S3 Standard), 20,000건의 Get 요청, 2,000건의 Put 요청, Delete 요청, 15GB의 데이터 전송이 무료

---

### 결론: 외부파일로 일단 프로젝트만들고 후에는 AWS S3사용해보기

**Toast ui image upload 기능 사용해보기**

![image](https://user-images.githubusercontent.com/48270067/150636819-d37dc214-ae85-4f55-b2f5-7c8ec585cc5c.png)


사용했더니 모든 컬럼에 null값이 들어가졌다. 

Toast Ui Editor는 이미지 업로드 시 이미지가 base64의 형태로 변환되어 업로드된다고한다. 따라서 이미지 업로드시 콜백 함수로 서버의 어떤 위치에 저장을 한 후에 해당 사진의 url을 리턴값으로 받아서 에디터에 출력시켜주어야한다.

* addImageHook()을 사용하라는 글을 보았다. 아직 미해결. 조금 더 예제를 보고 적용하려한다.


## Step 8

### post 상세 보기 페이지에서 수정, 삭제 기능 구현

**오류**

* localhost:8080/postlist(post 목록 페이지)에 이동했을때 해당 주소가 너무 많이 리디엑션되었다는 오류가 나왔다.
	* postlist 에서 삭제 후 새로고침이 되도록 하고 싶었다.
	*  postcontroller에서 post 목록 페이지로 맵핑되는 메소드 postlist()의 return 값을 redirection:/postlist로 했더니 에러가 나온것같다.
	* 그래서 지우고 postlist로 바꿨더니 해결

삭제 기능은 여차저차 잘 했는데 수정에서 막혔다.

* 수정버튼을 누르면 /writepost로 이동하여 일기 편집기가 보이고 이전에 저장해둔 내용이 보이게 하고 싶었다.
	* 블로그를 참고해보려고 tistory에서 수정을 눌렀더니 주소창에 "/manage/newpost/25?type=post&returnURL=수정하려는 페이지주소"가 추가되었다.
	* /editPost/{id} 페이지에 Model 객체를 사용해서 저장된 일기 내용을 가져왔다.


* JPA Repository save
	* .save()가 inser, update 둘 다 가능하다는 걸 알게되었다.
	* id가 없으면 save로 저장시 insert가 되는 것이고 id가 기존에 존재하면 merge() 메소드를 사용해 update가 된다.
	* 문제는 form을 사용해서 수정한 데이터를 POST 방식으로 보냈는데 id가 없어서 새로 저장된다는 점이다.
	* 아래 코드처럼 했더니 Id가 0이 나왔다. Id 값도 넘겨받아야하는데 form에는 id 값이 입력되는 칸이 없다. 그래서 form으로 제출할 때는 id 값을 가져오지 못하는 것이라고 생각한다.
	* ajax를 사용해야하나 고민중

```java
@PostMapping("/updatePost")
    public String updatePost(PostValueDTO.PostRequestDto PostRequestDto){
        System.out.println(PostREquestDto.getId());
        postService.updatePost(PostRequestDto);
```
