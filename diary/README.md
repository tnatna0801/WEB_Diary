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

## Step 7
