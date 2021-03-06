토비의 스프링 6장 마지막 내용정리

# 6.6 트랜잭션 속성

### 1. 트랜잭션 정의

트랜잭션: 더 이상 쪼갤 수 없는 최소 단위의 작업
* 트랜잭션 경계 안에서 진행된 작업은 commit()을 통해 모두 성공하든지 아니면 rollback()을 통해 모두 취소되어야한다.
* 이 밖에도 트랜잭션의 동작방식을 제어할 수 있는 조건이 있다. => 동작 방식에 영향을 줄 수 있는 네 가지 속성이 있다.

##### 트랜잭션 전파(Transacton propagation)

트랜잭션 전파란 트랜잭션의 경계에서 이미 진행 중인 트랜잭션이 있을 때 또는 없을 때 어떻게 동작할 것인가를 결정하는 방식을 말한다. 

* PROPAGATION_REQUIRED
  * 가장 많이 사용되는 전파 속성이다. 
  * 진행 중인 트랜잭션이 없으면 새로 시작하고, 이미 시작된 트랜잭션이 있으면 이에 참여한다. 
  * 다양한 방식으로 결합해서 하나의 트랜잭션으로 구성하기 쉽다.
* PROPAGATION_REQUIRES_NEW
  * 항상 새로운 트랜잭션을 시작한다.
  * 독립적인 트랜잭션이 보장돼야하는 코드에  적용할 수 있다.
* PROPAGATION_NOT_SUPPORTED
  * 트랜잭션 없이 동작하도록 만든다. 
  * 진행 중인 트랜잭션이 있어도 무시한다. 
  * 트랜잭션을 무시하는 속성을 두는 이유: 트랜잭션 경계설정은 보통 aop를 이용해 한 번에 많은 메소드에 동시에 적용하는 방법을 사용하는데 특별한 메소드만 트랜잭션 적용에서 제외하고 싶을때 사용한다.

##### 격리수준(Isolation level)

모든 DB 트랜잭션은 격리수준을 갖고 있어야한다. 서버환경에서는 여러개의 트랜잭션이 동시에 진행될 수 있다. 적절하게 격리수준을 조정해서 가능한 한 많은 트랜잭션을 동시에 진행시키면서도
문제가 발생하지 않게 하는 제어가 필요하다. 

* ISOLATION_DEFAULT: DataSource에 설정되어 있는 디폴트 격리 수준을 그대로 따른다는 뜻
* READ-UNCOMMITTED
* READ_COMMITTED
* REPEATABLE-READ
* SERIALIZABLE

##### 제한시간

* 트랜잭션을 수행하는 제한시간을 설정할 수 있다.
* 트랜잭션을 직접 시작할 수 있는 PROPAGATION_REQUIRED, PROPAGATION_REQIRES_NEW를 써야 의미가 있다. 

##### 읽기전용

* 읽기전용으로 설정해두면 트랜잭션 내에서 데이터를 조작하는 시도를 막아줄 수 있다. 
* 데이터 액세스 기술에 따라서 성능이 향상될 수도 있다.

### 2. 트랜잭션 인터셉터와 트랜잭션 속성

##### TransactionInterceptor

원하는 메소드만 선택해서 독자적인 트랜잭션 정의를 정용할 수 있는 방법은 없을까?
메소드별로 다른 트랜잭션 정의를 적용하려면 어드바이스의 기능을 확장해야 한다. 
메소드 이름 패턴에 따라 다른 트랜잭션 정의가 적용되도록 만드는 것이다. 

TransactionInterception
* 트랜잭션 정의를 메소드 이름 패턴을 이용해서 다르게 지정할 수 있는 방법을 추가로 제공해준다. 
* PlatformTransactionManager와 Properties 타입의 두가지 프로퍼티를 갖는다.
* Properties 타입인 프로퍼티 이름은 transactionAttributes로 트랜잭션 속성을 정의한 프로퍼티이다.
  * TransactionDefinition의 네가지 기본 항목 + rollbackOn() 를 갖고 있는 TrasactionAttribute 인터페이스로 정의된다.
  * rollbackOn() 메소드는 어떤 예외가 발생하면 롤백을 할 것인가를 결정하는 메소드
  * 이 TransactionAttribute를 이용하면 트랜잭션 부가기능의 동작방식을 모두 제어할 수 있다.

스프링이 제공하는 Transactioninterceptor에는 기본적으로 두 가지 종류의 예외처리 방식이 있다. 런타임 예외가 발생하면 트랜잭션은 롤백되고
반면에 타깃 메소드가 체크 예외를 던지는 경우에는 이것을 예외상황으로 해석하지 않고 일종의 비즈니스 로직에 따른, 의미가 있는 리턴 방식의 한 가지로 인식해서 트랜잭션을 커밋해버린다.

그런데 TransactionInterceptor의 이러한 예외처리 기본 원칙을 따르지 않는 경우가 있을 수 있다. 그래서 rollbackOn()이라는 
속성을 둬서 기본 원칙과 다른 예외처리가 가능하게 해준다. 
이를 활용하면 특정 체크 예외의 경우는 트랜잭션을 롤백시키고, 특정 런타임 예외에 대해서는 트랜잭션을 커밋시킬 수도 있다. 

##### 메소드 이름 패턴을 이용한 트랜잭션 속성 지정

Properties 타입의 transactionAttributes 프로퍼티는 메소드 패턴과 트랜잭션 속성을 키와 값으로 갖는 컬렉션이다. 
=> 컬렉션을 사용하는 이유는 메소드 패턴에 따라서 각기 다른 트랜잭션 속성을 부여할 수 있게 하기 위해서다. 
* PROPAGATION_NAME: 트랜잭션 전파 방식. PROPAGATION_으로 시작한다. (필수항목)
* ISOLATION_NAME: 격리수준. ISOLATION_으로 시작한다. (생략되면 디폴트 격리수준)
* readOnly: 읽기전용 항목 (생략가능)
* timeout_NNNN: 제한시간. timeout_으로 시작하고 초 단위 시간을 뒤에 붙인다. (생략가능)
* -Exception1: 체크 예외중 롤백 대상으로 추가할 것을 넣는다 (한개 이상 가능)
* +Exception1: 런타임 예외지만 롤백시키지 않을 예외를 넣는다 (한개 이상 가능)

이렇게 속성을 하나의 문자열로 표현하게 만든 이유는 트랜잭션 속성을 메소드 패턴에 따라 여러 개를 지정해줘야 하는데, 일일이 중첩된 태그와 프로퍼티로 설정하게 만들면 번거롭기 때문이다. 


### 3. 포인트컷과 트랜잭션 속성의 적용 전략

트랜잭션 부가기능을 적용할 후보 메소드를 선정하는 작업은 포인트컷에 의해 진행된다. 그리고 어드바이스의 트랜잭션 전파 속성에 따라서 메소드별로
트랜잭션의 적용방식이 결정된다. 

포인트컷 표현식과 트랜잭션 속성을 정의할 때 따르면 좋은 몇 가지 전략을 생각해보자. 

* 트랜잭션 포인트컷 표현식은 타입 패턴이나 빈 이름을 이용한다.  
* 공통된 메소드 이름 규칙을 통해 최소한의 트랜잭션 어드바이스와 속성을 정의한다. 

* (주의사항임)프록시 방식 AOP는 같은 타깃 오브젝트 내의 메소드를 호출할 때는 적용되지 않는다. 

### 4. 트랜잭션 속성 적용

트랜잭션 경계설정의 일원화

트랜잭션 경계설정의 부가기능을 여러 계층에서 중구난방으로 적용하는 건 좋지 않다. 
일반적으로 특정 계층의 경계를 트랜잭션 경계와 일치시키는 것이 바람직하다. 
비지니스 로직을 담고 있는 서비스 계층 오브젝트의 메소드가 트랜잭션 경계를 부여하기에 가장 적절한 대상이다.


# 6.7 애노테이션 트랜잭션 속성과 포인트컷

클래스나 메소드에 따라 제각각 속성이 다른, 세밀하게 튜닝된 트랜잭션 속성을 적용해야하는 경우가 가끔 존재한다.
이런 경우라면 메소드 이름 패턴을 이용해서 일괄적으로 트랜잭션 속성을 부여하는 방식은 적합하지 않다. 
기본 속성과 다른 경우가 있을 때마다 일일이 포인트컷과 어드바이스를 새로 추가해줘야 하기 때문이다.

=> 스프링이 제공하는 다른 방법: 직접 타깃에 트랜잭션 속성정보를 가진 애노테이션을 지정하는 방법

### 1. 트랜잭션 애노테이션

##### @Transactional

* @Transactional 애노테이션의 타깃은 메소드와 타입이다. => 메소드, 클래스, 인터페이스에 사용할 수 있다.
* 트랜잭션 속성정보로 사용하도록 지정하면 스프링은 @Transactional이 부여된 모든 오브젝트를 자동으로 타깃 오브젝트로 인식한다.
  * 이때 사용되는 포인트컷은 TransactionAttributeSourcePointcut이다. 
  * TransactionAttributeSourcePointcut은 스스로 표현식과 같은 선정기준을 갖고 있진 않다. 
  * 대신  @Transactional이 타입 레벨이든 메소드 레벨이든 상관없이 부여된 빈 오브젝트를 모두 찾아서 포인트컷의 선정 결과로 돌려준다.

##### 트랜잭션 속성을 이용하는 포인트컷

![image](https://user-images.githubusercontent.com/48270067/153228151-e2cc1ceb-f241-4fc5-8e00-afd0bdeb0b07.png)

TransactionInterceptor는 메소드 이름 패턴을 통해 부여되는 일괄적인 트랜잭션 속성정보 대신
@Transactional 애노테이션의 엘리먼트에서 트랜잭션 속성을 가져오는 AnnotationTransactionAttributeSource를 사용한다.
@Transactional은 메소드마다 다르게 설정할 수도 있으므로 매우 유연한 트랜잭션 속성 설정이 가능해진다.

트랜잭션 애노테이션으로 트랜잭션 속성이 부여된 오브젝트라면 포인트컷의 선정 대상이기도 하기 때문에 포인트 컷도 @Transactional을 통한 트랜잭션 속성정보를 참조하도록 만든다.


=> 위 방식을 사용하면 포인트컷과 트랜잭션 속성을 애노테이션 하나로 지정할 수 있다.

=> 트랜잭션 부가기능 적용 단위는 메소드이기 때문에 메소드마다 @Transactional을 부여하고 속성을 지정할 수 있다. 
* 이러면 유연한 속성 제어는 가능하겠지만 코드는 지저분해지고 동일한 속성 정보를 가진 애노테이션을 반복적으로 매소드마다 부여해주는 결과를 가져올 수 있다.
* 어떻게 해결? => 대체 정책


##### 대체 정책

스프링은 @Transactional을 적용할 때 4단계의 대체(fallback) 정책을 이용하게 해준다. 
메소드의 속성을 확인할 때 타깃 메소드, 타깃 클래스, 선언 메소드, 선언 타입(클래스, 인터페이스) 
순서에 따라서 @Transactional이 적용됐는지 차례로 확인하고 가장 먼저 발견되는 속성정보를 사용하게 하는 방법이다. 

```java
[1]
public interface Service{

	[2]
	void method1();
    [3]
    void method2();
}

[4]
public class ServiceImpl implements Service {

	[5]
	public void method1(){
    }
    [6]
    public void method2(){
    }
}
```
* 가장 먼저 타깃의 메소드에 @Transactional이 있는지 확인한다. 
  * 타깃 오브젝트 메소드인 [5], [6]이 첫번째 후보
* 부여되어 있다면 이를 속성으로 사용한다. 만약 없으면 다음 대체 후보인 타깃 클래스에 부여된 @Transactional 애노테이션을 찾는다.
  * 타깃 클래스인 [4]이 두번째 후보
* 타깃 클래스의 메소드 레벨에는 없었지만 클래스 레벨에 애노테이션이 존재한다면 이를 메소드의 트랜잭션 속성으로 사용한다. 
* 메소드가 선언된 타입까지 단계적으로 확인해서 @Transactional이 발견되면 적용하고, 끝까지 발견되지 않으면 해당 메소드는 트랜잭션 적용 대상이 아니라고 판단한다.
  * 인터페이스 메소드인 [2], [3]이 세번째 후보 (인터페이스도 메소드 먼저 확인한다.)
  * 인터페이스인 [1]이 네번째 후보


@Transactional을 사용하면 대체 정책을 잘 활용해서 애노테이션 자체는 최소한으로 사용하면서도 세밀한 제어가 가능하다.
* 기본적으로 @Transactional 적용 대상은 클라이언트가 사용하는 인터페이스가 정의한 메소드이므로 @Transactional 적용 대상은 
@Transactional도 타깃 클래스보다는 인터페이스에 두는 게 바람직하다. 
* 하지만 인터페이스를 사용하는 프록시 방식의 AOP가 아닌 방식으로 트랜잭션을 적용하면 인터페이에 정의한 @Transactional은 무시되기 때문에 안전하게 타깃 클래스에 @Transactional을 두는 방법을 권장한다.


# 6.8 트랜잭션 지원 테스트 

### 1. 선언적 트랜잭션과 트랜잭션 전파 속성

트랜잭션을 정의할 때 지정할 수 있는 트랜잭션 전파 속성은 매우 유용한 개념이다. 트랜잭션 전파라는 기법을 사용해 메소드는 독자적인 트랜잭션 단위가 
될 수도 있고(?), 다른 트랜잭션의 일부로 참여할 수도 있다.
* 불필요하게 코드를 중복하는 것도 피할 수 있고 애플리케이션을 작은 기능 단위로 쪼개서 개발할 수 있기 때문?

스프링은 트랜잭션 전파 속성을 선언적으로 적용할 수 있는 기능을 제공한다. 

* AOP를 이용해 코드 외부에서 트랜잭션의 기능을 부여해주고 속성을 지정할 수 있게 하는 방법을 **선언적 트랜잭션**이라고 한다.
* 반대로 TransactionTemplate이나 개별 데이터 기술의 트랜잭션 API를 사용해 직접 코드 안에서 사용하는 방법을 **프로그램에 의한 트랜잭션**이라고 한다.

### 2. 트랜잭션 동기화와 테스트

트랜잭션의 자유로운 전파와 그로 인한 유연한 개발이 가능할 수 있었던 기술적인 배경에는 AOP가 있다. 
AOP 덕분에 프록시를 이용한 트랜잭션 부가기능을 간단하게 애플리케이션 전반에 적용할 수 있었다.

또 한가지 중요한 기술적인 기반은 바로 스프링의 트랜잭션 추상화이다. 
* 하나의 트랜잭션으로 DAO에서 일어나는 작업들을 묶어서 추상레벨에서 관리하게 해줌

##### 트랜잭션 매니저와 트랜잭션 동기화

트랜잭션 추상화 기술의 핵심은 트랜잭션 매니저와 트랜잭션 동기화다. 
* 트랜잭션 매니저를 통해 트랜잭션 기술의 종류에 상관없이 일관된 트랜잭션 제어가 가능
* 트랜잭션 동기화 기술이 있었기에 시작된 트랜잭션 정보를 저장소에 보관해뒀다가 DAO에서 공유할 수 있었다.
  * 트랜잭션 동기화 기술은 트랜잭션 전파를 위해서도 중요한 역할을 한다. 
  * 진행 중인 트랜잭션이 있는지 확인하고, 트랜잭션 전파 속성에 따라서 이에 참여할 수 있도록 만들어주는 것도 트랜잭션 동기화 기술 덕분이다. 

```java
@Test
public void transactionSync() {
  userService.deleteAll();
  
  userService.add(users.get(0));
  userService.add(users.get(1));
}
```
테스트에서 각 메소드를 실행시킬 때는 기존에 진행중인 트랜잭션이 없고 트랜잭션 전파 속성은 REQUIRED이기 때문에 새로운 트랜잭션이 시작된다.
* deleteAll() 메소드가 실행되는 시점에서 트랜잭션이 시작, 메소드가 끝나면 트랜잭션 종료
*  그 후에 다시 add() 메소드가 호출되면 현재 실행중인 트랜잭션이 없으니 새로운 트랜잭션이 만들어짐
*  마지막 add() 메소드도 마찬가지
*  3개의 메소드가 실행되면 트랜잭션이 3개 생성됨

##### 트랜잭션 매니저를 이용한 테스트용 트랜잭션 제어

세 개의 트랜잭션을 하나로 통합할 수 없을까? 
* 세 개의 메소드 모두 트랜잭션 전파 속성이 REQUIRED이니 이 메소드들이 호출되기 전에 트랜잭션이 시작되게만 한다면 가능

### 3. 테스트를 위한 트랜잭션 애노테이션

##### @Transactional

AOP를 위한 것은 아니지만 기본적인 동작방식과 속성은 동일하다.

##### @Rollback

* 테스트용 @Transactional은 기본적으로 트랜잭션을 강제 롤백시키도록 설정되어 있다.
  * 애플리케이션의 클래스에 적용할 때와 가장 큰 차이점
* 트랜잭션을 커밋시켜서 테스트에서 진행한 작업을 그대로 DB에 반영하고 싶을 때 @Rollback(false) 사용

##### @TransactionConfiguration

* @TransactionConfiguraiton을 사용하면 롤백에 대한 공통 속성을 지정할 수 있다.
* 테스트 클래스의 모든 메소드에 트랜잭션을 적용하면서 모든 트랜잭션이 롤백되지 않고 커밋되게 하려면 일일히 메소드에 @Rollback(false)하지말고, 클래스레벨에 부여할 수 있는 @TransactionConfiguration 사용

##### @NotTransactional과 Propagation.NEVER

* @NotTransactional을 테스트 메소드에 부여하면 클래스 레벨의 @Transactional을 무시하고 트랜잭션을 시작하지 않은 채로 테스트를 진행
* @NotTransactional 대신 전파 속성을 Propagation.NEVER로 설정 가능

###### 효과적인 DB 테스트 

일반적으로 의존, 협력 오브젝트를 사용하지 않고 고립된 상태에서 테스트를 진행하는 단위 테스트와 DB 같은 외부의 리소스나
여러 계층의 클래스가 참여하는 통합 테스트는 아예 클래스를 구분해서 따로 만드는게 좋다. 
테스트는 어떤 경우에도 서로 의존하면 안 된다. 테스트가 진행되는 순서나 앞의 테스트의 성공 여부에 따라서 다음 테스트의 결과가 달라지는 테스트를 만들면 안 된다. 
코드가 바뀌지 않는 한 어떤 순서로 진행되더라도 테스트는 일정한 결과를 내야 한다.
