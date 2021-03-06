# 6.5 스프링 AOP

### 1. 자동 프록시 생성

투명한 부가기능을 적용하는 과정에서 발견된 문제점:

(투명하다-부가기능을 적용한 후에도 기존 설계와 코드에는 영향을 주지 않는다.)
* 부가기능이 타깃 오브젝트마다 새로 만들어지는 문제 => 스프링 ProxyFactoryBean의 어드바이스를 통해 해결
* 부가기능의 적용이 필요한 타깃 오브젝트마다 거의 비슷한 내용의 ProxyFactoryBean 빈 설정정보를 추가해주는 부분 => 해결해야함
  * 이런 류의 중복을 제거할 방법은 없을까? 

##### 중복 문제의 접근 방법
지금까지 반복적이고 기계적인 코드를 다뤘던 해결책을 생각해보자.

* JDBC API를 사용하는 DAO 코드 =(해결)=> 바뀌지 않는 부분과 바뀌는 부분을 구분해서 분리하고, 템플릿과 콜백, 클라이언트로 나누는 방법을 통해 깔끔하게 해결했다.(전략패턴과 DI를 적용)
* 반복적인 위임 코드가 필요한 프록시 클래스 코드
  * 프록시가 구현해야 하는 모든 인터페이스 메소드마다 타깃 오브젝트로의 위임코드와 부가기능 적용을 위한 코드가 반복적으로 필요했다.
  * 다이내믹 프록시라는 런타임 코드 자동생성 기법으로 해결
  * 변하지 않는 반복적인 코드는 다이내믹 프록시로 코드를 생성하고 변하는 부가 기능 코드(의미있는 부가기능)는 별도로 만들어서 다이내믹 프록시 생성 팩토리에 DI로 제공하는 방법을 이용

==> 반복적인 ProxyFactoryBean 설정문제도 설정 자동등록 기법으로 해결할 수 있지 않을까? 


##### 빈 후처리기를 이용한 자동 프록시 생성기

스프링은 컨테이너로서 제공하는 기능 중에서 변하지 않는 핵심적인 부분외에는 대부분 확장할 수 있도록 확장 포인트를 제공해준다. 
그중에서 관심을 가질 만한 확장 포인트는 BeanPostProcessor 인터페이스를 구현해서 만드는 빈 후처리기다.

빈 후처리기는 스프링 빈 오브젝트로 만들어지고 난 후에, 빈 오브젝트를 다시 가공할 수 있게 해준다. 

빈 후처리기 중 하나인 DefaultAdvisorAutoProxyCreator
* 어드바이저를 사용한 자동 프록시 생성기
* 빈 후처리기는 빈 오브젝트의 프로퍼티를 강제로 수정할 수도 있고 별도의 초기화 작업을 수행할 수도 있다.
* 만들어진 빈 오브젝트 자체를 바꿔치기할 수도 있다. 
* 따라서 스프링이 설정을 참고해서 만든 오브젝트가 아닌 다른 오브젝트를 빈으로 등록 시키는 것이 가능하다.

=> 스프링이 생성하는 빈 오브젝트의 일부를 프록시로 포장하고, 프록시를 빈으로 대신 등록할 수도 있다. 

그럼 자동으로 프록시를 생성할 수 있다.

**빈 후처리기를 이용한 프록시 자동 생성 방법**

1. DefaultAdvisorAutoProxyCreator 빈 후처리기가 등록되어 있으면 스프링은 빈 오브젝트를 만들 때마다 후처리기에 빈을 보낸다.
2. 빈으로 등록된 모든 어드바이저 내의 포인트컷을 확인하여 적용대상인지 확인한다.
3. 적용 대상이면 내장된 프록시 생성기에게 현재 빈에대한 프록시를 만들게 한다.
4. 만들어진 프록시에 어드바이저를 연결해준다.
5. 프록시가 생성되면 원래 컨테이너가 전달해준 빈 오브젝트 대신 프록시 오브젝트를 컨테이너에게 돌려준다.


##### 확장된 포인트컷

포인트 컷은 어떤 메소드에 부가기능을 적용할지 선정해주는 역할이라고했는데 위에 생성방법에선 포인트컷이 등록된 빈 중에서 어떤 빈에 프록시를
적용할지를 선택한다는데 이상하다. ==> 두 가지 기능 모두 가지고 있다. 


**포인트 컷이 가진 두 가지 기능**

```java
public interface Pointcut{
  ClassFilter getClassFilter();      //프록시를 적용할 클래스인지 확인해 준다.
  MethodMatcher getMethodMatcher(); // 어드바이스를 적용할 메소드인지 확인해준다.
}
```

만약 포인트 컷 선정 기능을 모두 적용한다면 먼저 프록시를 적용할 클래스인지 판단하고 나서, 적용 대상 클래스인 경우에는 어드바이스를 적용할 메소드인지 확인하는 식으로 동작한다.

ProxyFactoryBean에서는 굳이 클래스 레벨의 필터는 필요 없었지만, 모든 빈에 대해 프록시 자동 적용 대상을 선별해야 하는 빈 후처리기인 DefaultAdvisorAutoProxyCreator는 클래스와 메소드 선정 알고리즘을 모두 갖고 있는 포인트컷이 필요하다. 
정확히는 그런 포인트컷과 어드바이스가 결합되어 있는 어드바이저가 등록되어 있어야 한다.



### 2. DefaultAdvisorAutoProxyCreator의 적용

실습

##### 클래스 필터를 적용한 포인트컷 작성

메소드 이름만 비교하던 포인트컷인 NameMatchMethodPointcut을 상속해서 프로퍼티로 주어진 이름 패턴을 가지고 클래스 이름을 비교하는 ClassFilter를 추가하도록 만든다.

##### 어드바이저를 이용하는 자동 프록시 생성기 등록

DefaultAdvisorAutoProxyCreator 등록

```java
<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" />
```

##### 포인트컷 등록

기존의 포인트컷 설정을 삭제하고 새로 만들 클래스 필터 지원 포인트컷을 빈으로 등록한다.

##### 어드바이스와 어드바이저

어드바이스인 transactionAdvice 빈의 설정은 수정할게 없다. 어드바이저인 transactionAdvisor 빈도 수정할 필요는 없다. 하지만 어드바이저로 사용되는 방법이 바뀌었다는 사실은 기억해두자.

이제는 transactionAdvisor를 명시적으로 DI하는 빈은 존재하지 않는다. 
대신 어드바이저를 이용하는 자동 프록시 생성기인 DefaultAdvisorAutoProxyCreator에 의해 자동 수집되고, 프록시 대상 선정 과정에 참여하며, 자동 생성된 프록시에 다이내믹하게 DI돼서 동작하는 어드바이저가 된다.

##### ProxyFactoryBean 제거와 서비스 빈의 원상복구

프록시를 도입했던 때부터 아이디를 바꾸고 프록시에 DI 돼서 간접적으로 사용돼야 됐던 userServiceImpl 빈의 아이디를 다시 userService로 되돌려놓을 수 있다. 
더 이상 명시적인 프록시 팩토리 빈을 등록하지 않기 때문이다.

### 3. 포인트컷 표현식을 이용한 포인트컷

좀 더 편리한 포인트컷 작성 방법을 알아보자.

##### 포인트컷 표현식

포인트컷 표현식을 지원하는 포인트컷을 적용하려면 AspectJExpressionPointcut 클래스를 사용하면 된다. 
AspectJExpressionPointcut은 클래스와 메소드의 선정 알고리즘을 포인트컷 표현식을 이용해 한 번에 지정할 수 있게 해준다. 
AspectJ의 일부 문법을 확장해서 사용하는것이다. 그래서 이를 AspectJ 포인트컷 표현식이라고도 한다.



### 4. AOP란?

UserService에 트랜잭션을 적용해온 과정을 정리해보자.
* 트랜잭션 서비스 추상화
  * 트랜잭션 경계설정 코드를 비즈니스 로직을 담은 코드에 넣으면서 맞닥뜨린 첫 번째 문제는 특정 트랜잭션 기술에 종속되는 코드가 되어버린다는 것이다.
  * 트랜잭션 적용이라는 추상적인 작업내용은 유지한 채로 구체적인 구현 방법을 자유롭게 바꿀 수 있도록 서비스 추상화 기법을 적용했다.
  * 결국 인터페이스와 DI를 통해 무엇을 하는지 남기고, 그것을 어떻게 하는지를 분리한 것이다.
* 프록시와 데코레이터 패턴
  * DI를 이용한 데코레이터 패턴 적용
  * 클라이언트가 인터페이스와 DI를 통해 접근하도록 설계하고, 데커레이터 패턴을 적용해서, 비즈니스 로직을 담은 클래스의 코드에는 전혀 영향을 주지 않으면서 트랜잭션이라는 부가기능을 자유롭게 부여할 수 있는 구조를 만들었다.
  * 트랜잭션을 처리하는 코드는 일종의 데코레이터에 담겨서, 클라이언트와 비즈니스로직을 담은 타깃 클래스 사이에 존재하도록 만들었다. 
  * 그래서 클라이언트가 프록시 역할을 하는 트랜잭션 데코레이터를 거쳐서 타깃에 접근할 수 있게 됐다.
* 다이내믹 프록시와 프록시 팩토리 빈
  * 비즈니스 로직 인터페이스의 모든 메소드마다 트랜잭션 기능을 부여하는 코드를 넣어 프록시 클래스를 만드는 작업이 오히려 큰 점이 됐다.
  * 그래서 프록시 클래스 없이도 프록시 오브젝트를 런타임 시에 만들어주는 JDK 다이내믹 프록시 기술을 적용했다.
  * 하지만 동일한 기능의 프록시를 여러 오브젝트에 적용할 경우 오브젝트 단위로는 중복이 일어나는 문제는 해결하지 못했다.
  * 스프링의 프록시 팩토리 빈을 이용해서 다이내믹 프록시 생성 방법에 DI를 도입했다. 
  * 내부적으로 템플릿/콜백 패턴을 활용하는 스프링의 프록시 팩토리 빈 덕분에 부가기능을 담은 어드바이스와 부가기능 선정 알고리즘을 담은 포인트컷은 프록시에서 분리될 수 있었고 여러 프록시에서 공유해서 사용할 수 있게 됐다.
* 자동 프록시 생성 방법과 포인트 컷
  * 트랜잭션 적용대상이 되는 빈마다 일일이 프록시 팩토리 빈을 설정해줘야 한다는 부담이 남아있다.
  * 이를 해결하기 위해서 스프링 컨테이너의 빈 생성 후처리 기법을 활용해 컨테이너 초기화 시점에서 자동으로 프록시를 만들어주는 방법을 도입했다.

##### 부가기능의 모듈화

트랜잭션 적용 코드는 기존에 써왔던 방법으로는 간단하게 분리해서 독립된 모듈로 만들 수가 없었다.
왜냐하면 트랜잭션 경계설정 기능은 다른 모듈의 코드에 부가적으로 부여되는 기능이라는 특징이 있기 때문이다.
(부가기능이기 때문에 독립적인 방식으로 적용되기 어렵기 때문) 그래서 트랜잭션 코드는 한데 모을 수 없고, 
어플리케이션 전반에 여기저기 흩어져있다. 따라서 다이내믹 프록시라든가 빈 후처리 기술과 같은 복잡한 기술이 요구 된다. 

어떻게 이런 부가기능을 독립적인 모듈로 만들 수 있을까?

위에서 정리한 개념들이 이런 문제를 해결하기 위해 적용한 대표적인 방법이다. 
결국 지금까지 해온 모든 작업은 핵심기능에 부여되는 부가기능을 효과적으로 모듈화 하는 방법을 찾는 것이었고, 
어드바이스와 포인트컷을 결합한 어드바이저가 단순하지만 이런 특성을 가진 모듈의 원시적인 형태로 만들어지게 됐다.

##### AOP: 애스펙트 지향 프로그래밍
(관점 지향 프로그래밍)

부가기능 모듈화 작업은 기존의 객체지향 설계 패러다임과는 구분되는 새로운 특성이 있다고 생각해 이런 부가기능 모듈을 애스펙트라는 특별한 이름으로 부르기 시작했다.
애스펙스란 그 자체로 애플리케이션의 핵심기능을 담고 있지는 않지마, 애플리케이션을 구성하는 중요한 한 가지 요소이고, 핵심기능에 부가되어 의미를 갖는 특별한 모듈을 가리킨다.

애스펙트는 
* 부가될 기능을 정의한 코드인 어드바이스와, 어디에 적용할지를 결정하는 포인트컷을 함께 갖고 있다.
* 단어 그대로 애플리케이션을 구성하는 한 가지 측면이라고 생각 할 수 있다.

기존의 코드는 핵심기능과 부가기능이 같이 들어있기에 코드가 복잡하고 지저분했지만, 
독립된 측면에 존재하는 애스펙트로 분리한 덕에 (입체적구조로 가져가면서) 
각각 성격이 다른 부가기능은 다른면에 존재하도록 만들었다.

이렇게 핵심적인 기능에서 부가적인 기능을 분리해서 애스펙트라는 독특한 모듈로 만들어서 설계하고 개발하는 방법을 AOP 라고 부른다.

부가기능의 관심 중복이 횡단으로 나타나기 때문에 이것을 횡단관심사이라고 한다. 
관심 지향 프로그래밍은 이런 횐단관심사에 따라 프로그래밍한다고 할 수 있다. 

이름만 보고 OOP와 대치될 것같다는 느낌을 받았는데 오히려 더 객체지향적으로 코드를 구성할 수 있도록 보완해준다.
어떤 프로그램 구조에 대해서 다른 생각의 방향을 제시함으로써 AOP가 OOP를 보완하고 있다. 


##### AOP 적용 기술

* 프록시를 이용한 AOP
  * 독립적으로 개발한 부가기능 모듈을 타깃 메서드에 다이내믹하게 적용해주기 위해 가장 중요한 역할을 맡고있는 게 바로 프록시다.
  * 그래서 스프링 AOP는 프록시 방식의 AOP라고 할 수 있다.  
* 바이트코드 생성과 조작을 통한 AOP
  * AspectJ(자바의 AOP 구현체)는 JVM 로딩시점을 가로채서 바이트코드를 조작한다.(복잡한 방법)
  * 바이트코드조작 방법을 사용하는 이유
    * 바이트코드를 조작해서 타깃 오브젝트를 직접 수정해버리면 자동 프록시 생성 방식을 사용하지 않아도 AOP를 적용할 수 있기 때문이다. 즉, 컨테이너가 사용되지 않는 환경에서도 가능하다.
    * 프록시 방식보다 훨씬 강력하고 유연한 AOP가 가능하기 때문이다. 그래서 다양한 작업에 부가기능을 부여 가능하다.

##### AOP의 용어

부가기능은 횡단의 관심을 가지기 때문에 필수적으로 어떤 부가기능을 어디에 적용할 것인가라는 질문을 만나게된다.

* 타깃
  * 어떤 대상에 부가 기능을 부여할 것인가?
  * 부가기능을 부여할 대상을 뜻한다.
* 어드바이스
  * 어떤 기능을 부가적으로 부여할 것인가?
  * 타깃에게 제공할 부가기능을 담은 모듈
  * Before, AfterReturning, AfterThrowing, After, Around
* 조인 포인트
  * 어디에 적용할 것인가? 
  * 어드바이스가 적용될 수 있는 위치를 말한다.
  * **메소드**, 필드, 객체, 생성자 등
  * 스프링 AOP에서는 메소드가 실행될때만으로 한정하고 있다.
* 포인트 컷
  * 어드바이스를 적용할 조인 포인트를 선별하는 작업 또는 그 기능을 적의한 모듈을 말한다.
* 프록시
  * 클라이언트와 타깃 사이에 존재하면서 부가기능을 제공하는 오브젝트
  * DI를 통해 타깃 대신 클라이언트에게 주입되며, 클라이언트의 메소드 호출을 대신 받아서 타깃에 위임해주면서, 그 과정에서 부가기능을 부여한다. 
* 어드바이저
  * 포인트컷과 어드바이스를 하나씩 갖고 있는 오브젝트다.
  * 어떤 부가기능(어드바이스)을 어디(포인트컷)에 전달할 것인가를 알고 있는 AOP의 가장 기본이 되는 모듈이다.
  * 스프링 AOP에서만 사용되는 특별한 용어이고, 일반적인 AOP에서는 사용되지 않는다.
* 애스팩트
  * AOP의 기본 모듈
  * 한 개 또는 그 이상의 포인트컷과 어드바이스의 조합으로 만들어지며 보통 싱글톤 형태의 오브젝트로 존재한다. 
  * 따라서 클래스와 같은 모듈 정의와 오브젝트와 같은 실체(인스턴스)의 구분이 특별히 없다.




##### AOP 네임스페이스

스프링의 프록시 방식 AOP를 적용하려면 최소한 네 가지 빈을 등록해야한다.

* 자동 프록시 생성기
  * DefaultAdvisorAutoProxyCreator 클래스를 빈으로 등록한다. 다른 빈을 DI하지도 않고 DI되지도 않기 때문에 id도 굳이 필요없다. 빈 오브젝트를 생성하는 과정에 빈 후처리기로 참여한다.
* 어드바이스
  * 부가기능을 구현한 클래스를 빈으로 등록한다.
* 포인트컷
  * 스프링의 AspectJExpressionPointcu을 빈으로 등록하고 expression 프로퍼티에 포인트컷 표현식을 넣어주면 된다.
* 어드바이저 
  * DefaultPointcutAdvisor 클래스를 빈으로 등록해서 사용한다. 어드바이스와 포인트컷을 프로퍼티로 참조하는 것 외에는 기능은 없다. 
  * 자동프록시 생성기에 의해 자동 검색되어 사용된다.

**AOP 네임스페이스**

스프링은 AOP와 관련된 태그를 정의해둔 aop 스키마를 제공한다. aop 스키마에 정의된 태그는 별도의 네임스페이스를 지정해서 디폴트 네임스페이스의 <bean> 태그와 구분해서 사용할 수 있다.
aop 네임스페이스를 선언하면 AOP 설정 빈을 간단히 바꿀 수 있다. 
  
```java
<aop:config> // AOP 설정을 담는 부모태그다. 필요에 따라 AspectJAdvisorAutoProxyCreator를 빈으로 등록해준다.
  <aop:pointcut id=”transactionPointcut” // expression의 표현식을 프로퍼티로 가진 AspectJExpressionPointcut을 빈으로 등록해준다.
                expression=”execution(* *..*ServiceImpl.upgrade*(..)) /”>
  <aop:advisor advice-ref=”transactionAdvice” pointcut-ref=”transactionPointcut” /> // advicce와 pointcut의 ref를 프로퍼티로 갖는 DefaultBeanFactoryPointcutAdvisor를 등록해준다.
</aop:config>
```











