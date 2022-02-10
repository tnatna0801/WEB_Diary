## 6.3 다이내믹 프록시와 팩토리 빈

### 1. 프록시와 프록시 패턴, 데코레이터 패턴


**프록시**

트랜잭션 적용 사실 자체를 분리했을 때 클라이언트가 핵심기능을 가진 클래스를 직접 사용해버리면 부가기능이 적용될 기회가 없다. 
그래서 부가기능은 마치 자신이 핵심기능을 가진 클래스인 것처럼 꾸며서, 클라이언트가 자신을 거쳐서 핵심기능을 사용하도록 만들어야 한다. 

![image](https://user-images.githubusercontent.com/48270067/151207980-ae406769-a7e4-439c-9fb3-e9f6f476ed37.png)
* 프록시는 클라이언트가 사용하려고 하는 실제 대상인 것처럼 위장해서 클라이언트의 요청을 받아주는 대리자(UserServiceTx)이다.
* 타킷은 프록시를 통해 최종적으로 요청을 위임받아 처리하는 실제 오브젝트(UserServiceImpl)이다.
* 프록시의 특징은 타깃과 같은 인터페이스를 구현했다는 것과 프록시가 타깃을 제어할 수 있는 위치에 있다는 것이다.

**프록시의 사용목적**
1. 클라이언트가 타깃에 접근하는 방법을 제어하기 위함
2. 부가적인 기능을 부여해주기 위함

두 가지 모두 프록시를 사용한다는 점은 동일하지만, 목적에 따라서 디자인 패턴에서는 다른 패턴으로 구분한다.


**데코레이터 패턴과 프록시 패턴**

1. 데코레이터 패턴
  * 타깃에 부가적인 기능을 런타임 시 다이내믹하게 부여해주기 위해 프록시를 사용하는 패턴
    * 다이내믹하게 기능을 부가한다는 의미는 코드상에서는 어떤 방법과 순서로 프록시와 타깃이 연결되어 사용되는지 정해져 있지 않다는 뜻이다.
   * 실제 내용물은 동일하지만 부가적인 효과를 부여해줄 수 있다. => 프록시가 꼭 한 개로 제한되지 않는다.
   * 프록시가 직접 타깃을 사용하도록 고정시킬 필요가 없음
     * 이를 위해 데코레이터 패턴에서는 같은 인터페이스를 구현한 타깃과 여러 개의 프록시를 사용할 수 있다.
     * 여러 개의 프록시를 단계적으로 위임하는 구조로 사용
   * 데코레이터 패턴은 인터페이스를 통해 위임하는 방식이기 때문에 어느 데코레이터에서 타깃으로 연결되는지 코드 레벨에선 알지 못한다.    
   * 데코레이터 패턴은 타깃의 코드를 손대지 않고, 클라이언트가 호출하는 방법도 변경하지 않은 채로 새로운 기능을 추가할 때 유용한 방법이다.

2. 프록시 패턴
  * 프록시 != 프록시 패턴
  * 프록시는 클라이언트와 사용 대상 사이에 대리 역할을 맡은 오브젝트를 두는 방법을 총칭
  * 타깃에 대한 접근방법을 제어하려는 목적을 가진 경우 사용하는 것이 프록시 패턴이다.
  * 프록시 패턴의 프록시(?)는 타깃의 기능을 확장하거나 추가하지 않는다. 대신 클라이언트가 타깃에 접근하는 방식을 변경해준다.
  
  * 사용예시
    * 타깃 오브젝트에 대한 레퍼런스가 미리 필요할 경우(?)
    * 원격 오브젝트를 이용하는 경우: 원격 오브젝트에 대한 프록시를 만들어두고, 클라이언트는 마치 로컬에 존재하는 오브젝트를 쓰는 것처럼 프록시를 사용
    * 타깃에 대한 접근권한을 제어: 수정 가능한 오브젝트가 있는데, 특정 레이어로 넘어가서는 읽기전용으로만 동작하게 강제해야 하는 경우

---

### 2. 다이내믹 프록시

**프록시의 구성과 프록시 작성의 문제점**
* 타깃 오브젝트로 위임하는 코드 작성의 번거로움
  * 필요 없는 메소드도 구현해서 일일이 위임해야함
* 부가기능 코드가 중복될 가능성이 많다는 점
  * 트랜잭션 코드는 대부분의 로직에 적용될 필요가 있음
  * 메소드가 늘어나고 트랜잭션 적용 비율이 높아지면, 유사한 코드가 여러 메소드에 중복해서 나타남 

=> 다이나믹 프록시로 문제를 해결할 수 있다. 

**리플렉션**

다이내믹 프록시는 리플렉션 기능을 이용해 프록시를 만들어 준다. 리플렉션은 자바의 코드 자체를 추상화해서 접근하도록 만든 것이다.

(왜 리플렉션 기능을 이용해 프록시를 만들면 문제를 해결할 수 있는 것인지...)

```java
Method lengthMethod = String.classs.getMethod("length");
```

**다이내믹 프록시의 적용**

![image](https://user-images.githubusercontent.com/48270067/151216955-01730a05-7455-4c0d-aa24-f4c94e1143ff.png)

다이내믹 프록시는 프록시 팩토리에 의해 런타임 시 다이내믹하게 만들어지는 오브젝트다.
클라이언트는 다이내믹 프록시 오브젝트를 타깃 인터페이스를 통해 사용할 수 있다.
* 프록시 팩토리에게 인터페이스 정보만 제공해주면 해당 인터페이스를 구현한 클래스의 오브젝트를 자동으로 만들어주기 때문에 인터페이스를 모두 구현하는 수고를 할 필요가 없다.

다이나믹 프록시가 인터페이스 구현 클래스의 오브젝트는 만들어주지만, 프록시로 필요한 부가기능 제공 코드는 직접 작성해야한다.(InvocationHandler)
* 타깃 인터페이스의 모든 메소드 요청이 하나의 메소드로 집중되기 때문에 중복되는 기능을 효과적으로 제공할 수 있다.

**다이내믹 프록시의 확장**

이 전 코드보다 어려워보이는데 장점이 있을까?

다이나믹 프록시 방식이 직접 정의해서 만든 프록시보다 훨씬 유연하고 많은 장점이 있다.
* Hello 인터페이스의 메소드가 3개가 아니라 30개로 늘어난다면 다이내믹 프록시를 생성해서 사용하는 코드는 손댈 것이 없다. 
  * invoke()로 부가기능을 처리가능 하다.  
* 타깃의 종류에 상관없이도 적용이 가능하다. Method 인터페이스를 이용해 타깃의 메소드를 호출하는 것이므로 Hello 타입의 타깃으로 제한할 필요는 없다. 
* 리턴 타입뿐만 아니라 메소드 이름으로 조건을 걸 수 있다.


---

### 4. 다이내믹 프록시를 위한 팩토리 빈

어떤 타깃에도 적용 간으한 트랜잭션 부가기능을 담은 TransasctionHandler를 만들었고 이를 이용하는 다이내믹 프록시를 UserService에 적용하는 테스트를 만들어봤다.

TransactionHandler와 다이내믹 프록시를 스프링의 DI를 통해 사용할 수 있도록 만들어야 할 차례이다.
* 다이내믹 프록시 오브젝트는 일반적인 스프링의 빈으로는 등록할 방법이 없다. 스프링의 빈은 기본적으로 클래스 이름과 프로퍼티로 정의된다. 그러나, 다이내믹 프록시는 Proxy 클래스의 newProxyInstance()라는 스태틱 팩토리 메소드를 통해서만 만들 수 있다.


**팩토리 빈**

스프링에서 빈을 만드는 법
1. 클래스 정보를 가지고 디폴트 생성자를 통해 오브젝트를 만드는 방법
2. 팩토리 빈을 이용해 생성 (!!!!!)
  * 팩토리 빈: 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 특별한 빈을 말한다.
  * 팩토리 메소드를 가진 오브젝트
  * FactoryBean을 구현한 클래스가 빈으로 등록되면, 팩토리 빈 클래스의 오브젝트를 getObject()를 이용해 가져오고, 이를 빈 오브젝트로 사용한다.
  * 빈의 클래스로 등록된 팩토리 빈은 빈 오브젝트를 생성하는 과정에서만 사용된다.   


**다이내믹 프록시를 만들어주는 팩토리 빈**

팩토리 빈 사용법
* getObject() 메소드에 다이내믹 프록시 오브젝트를 만들어주는 코드를 삽입
* 스프링 빈에는 팩토리 빈과 UserServiceImpl만 등록


### 5. 프록시 팩토리 빈 방식의 장점과 한계

**프록시 팩토리 빈의 재사용**

TransactionHandler를 이용하는 다이내믹 프록시를 생성해주는 FactoryBean은 코드의 수정 없이도 다양한 클래스에 적용할 수 있다. 
타깃 오브젝트에 맞는 프로퍼티 정보를 설정해서 빈으로 등록해주기만하면 된다. (???)

프록시 팩토리 빈을 이용하면 프록시 기법을 아주 빠르고 효과적으로 적용할 수 있다.

**프록시 팩토리 빈 방식의 장점**

데코레이터 패턴 적용의 문제점을 해결
* 타깃 인터페이스를 구현하는 클래스를 일일이 만드는 번거로움 제거
* 부가 기능 코드의 중복 문제 해결

**프록시 팩토리 빈의 한계**

프록시를 통해서 타깃에 부가기능을 제공하는 것은 메소드 단위로 일어나는 일이다.

* 한 번에 여러 개의 클래스에 공통적인 부가기능을 제공이 불가능하다.
  * 트랜잭션과 같이 비즈니스 로직을 담은 많은 클래스의 메소드에 적용할 필요가 있다면 거의 비슷한 프록시 팩토리 빈의 설정이 중복되는 것을 막을 수 없다.
* 하나의 타깃에 여러 개의 부가기능을 적용하려고 할 때도 문제
  * 빈 설정이 부가기능의 개수만큼 붙어야한다.
* TransactionHandler 오브젝트가 프록시 팩토리 빈 개수만큼 만들어진다. 

---
기존 코드의 수정 없이 트랜잭션 부가기능을 추가해줄 수 있는 다양한 방법을 살펴봤다.

스프링의 해결책을 알아볼 차례이다.

## 6.4 스프링의 프록시 팩토리 빈

자바에는 JDK에서 제공하는 다이내믹 플록시 외에도 편리하게 프록시를 만들 수 있도록 지원해주는 다양한 기술이 존대한다. 
따라서 스프링은 프록시 오브젝트를 생성해주는 기술을 추상화한 팩토리 빈을 제공해준다.

* 스프링의 ProxyFactoryBean은 프록시를 생성해서 빈 오브젝트로 등록하게 해주는 팩토리 빈이다. 프록시를 생성하는 작업만을 담당하고 프록시를 통해 제공해줄 부가기능은 별도의 빈에 둘 수 있다.
* ProxyFactoryBean이 생성하는 프록시에서 사용할 부가기능은 MethodInterceptor 인터페이스를 구현해서 만든다. 
* MethodInterceptor의 invoke()는 ProxyFactoryBean으로부터 타깃 오브젝트에 대한 정보까지도 함께 제공받는다.(이전의 InvocationHandler의 invoke()는 타깃 오브젝트에 대한 정보를 제공하지 않는다.)


**어드바이스: 타깃이 필요없는 순수한 부가기능**

ProxyFactoryBean을 적용한 코드 vs JDK 다이내믹 프록시를 사용했던 코드
* 타깃 오브젝트가 등장하지 않고 메소드 정보와 타깃 오브젝트가 담긴 MethodInvocation 오브젝트가 전달되기 때문에 부가기능을 제공하는 데만 집중할 수 있다.(??)
* addAdvice() 메소드를 사용해 ProxyFactoryBean 하나만으로 여러 개의 부가기능을 제공해주는 프록시를 만들 수 있다. 팩토리 빈의 단점 중 하나였던, 새로운 부가기능을 추가할 때마다 프록시와 프록시 팩토리 빈도 추가해줘야 한다는 문제를 해결할 수 있다. 

(==>MethodInvocation은 일종의 콜백 오브젝트로, proceed() 메소드를 실행하면 타깃 오브젝트의
메소드를 내부적으로 실행해주는 기능이 있다.)

(Advice: MethodInterceptor처럼 타깃 오브젝트에 적용하는 부가기능을 담은 오브젝트를 스프링에서 부르는 말,
 타깃 오브젝트에 종속되지 않는 순수한 부가기능을 담은 오브젝트)

**포인트컷: 부가기능 적용 대상 메소드 선정 방법**

직접 구현했을 때에는 부가기능 적용 외에도 메소드의 이름을 가지고 부가기능 적용 대상 메소드를 선정하는 작업이 필요했다.

스프링의 ProxyFactoryBean 사용 방식에서 메소드 선정 기능 넣는 방법
* MethodInterceptor는 여러 프록시가 공유해서 사용할 수 있도록 하기 위해서 타깃 정보를 갖고 있지 않도록 만들었다.
* 그래서 싱글톤 빈으로 등록할 수 있었다.
* 여기에 트랜잭션 적용 대상 메소드 이름 패턴을 넣어주는 것은 곤란

==> 함께 두기 곤란한 성격이 다르고 변경 이유와 시점이 다르고, 생성 방식과 의존관계가 다른 코드가 함께 있다면 분리!!

스프링의 ProxyFactoryBean 방식은 두 가지 확장 기능인 부가기능(Advice)와 메소드 선정 알고리즘(Pointcut)을 활용하는 유연한 구조를 제공한다.
* Advice(어드바이스): 부가기능을 제공하는 오브젝트
* Pointcut(포인트 컷): 메소드 선정 알고리즘을 담은 오브젝트
* 둘 다 모두 프록시에 DI로 주입돼서 사용된다.

프록시의 동작 순서
1. 클라이언트로부터 요청
2. 포인트컷에 부가기능을 부여할 메소드인지 확인 요청
3. 부가기능 부여 오브젝트인 경우 MethodInterceptor 타입의 어드바이스 호출

포인트컷을 함께 등록할 때는 어드바이스와 포인트컷을 Advisor 타입으로 묶어서 addAdvisor() 메소드를 호출해야 한다. 
왜냐하면 ProxyFactoryBean에는 여러 개의 어드바이스와 포인트컷이 추가될 수 있기 때문이다. 
따로 등록하면 어떤 어드바이스(부가기능)에 대해 어던 포인트컷(메소드 선정)을 적용할지 애매해지기 때문이다. 
* Advisor(어드바이저)=pointcut(메소드 선정 알고리즘) + advice(부가기능)

**어드바이스와 포인트컷의 재사용**

ProxyFactoryBean은 스프링의 ID와 템플린/콜백 패턴, 서비스 추상화 등의 기법이 모두 적용되었기 때문에 독립적이며, 여러 프록시가 공유할 수 있는 어드바이스와 포인트컷으로 확장 기능을 분리할 수 있었다. 

이미 만들어둔 TransactionAdvice를 그대로 재사용할 수 있다. 
* 포인트 컷이 필요하면 이름 패턴만 지정해서 ProxyFactoryBean에 등록하면 된다. 

트랜잭션 부가기능을 담은 TransactionAdvice는 하나만 만들어서 싱글톤 빈으로 등록해주면 DI 설정을 통해 모든 서비스에 적용이 가능하다. (???)







