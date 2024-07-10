# Cache을 이용한 성능개선 프로젝트
### 😊 예시
#### 예시


#### 예시예시

#### 예시예시

### 😊 트러블슈팅 예시






## 📈 과제 요구사항
### 구현 기능(요구사항)

- [ ]  **앞서 개발한 검색 API 에 In-memory Cache(Local Memory Cache) 적용**
    - `spring-boot-starter-cache`  의존성을 이용하고 Spring AOP 방식으로 동작하는 `@Cacheable`  어노테이션을 활용해 구현할 것
    - 기존 API 를 지우는 것이 아닌 새롭게 v2 API 를 추가
    - **총 2개의 검색 API 가 존재해야한다.**
        - v1 API 는 기존에 Cache 가 적용되지 않은 API 이고, (`/api/v1/board/search` )
        v2 API 는 Redis Cache(선택 구현 기능) 가 적용된 API 가 되어야 한다. (`/api/v2/board/search` )
    - **왜 검색 API 에 Cache 를 적용했는지 스스로 꼭!! 고민하고 Readme 작성.**
    - 검색 API 가 아니더라도 Cache 를 적용할만한 포인트가 있다면 함께 적용해보자.
        - 단, 해당 포인트에 왜(Why!?) Cache 를 적용했는지 설명할 수 있어야한다.

### 2️⃣  선택 구현 기능
- [ ]  **In-memory Cache 가 적용된 v2 검색 API 를 Redis 를 이용한 Remote Cache 로 수정**
    - 이미 Cache 가 적용되어있는 v2 API 를 왜 굳이 Redis Cache 로 수정해야하는지 이유를 고민해보자. (Hint. Scale-out)
    
- [ ]  **성능테스트를 위해 대용량 Dummy 데이터 적재하기**
    - **검색에 사용되는 Database Table 에 Dummy 데이터 Insert 하기**
      
- [ ]  **Redis Cache 를 적용한 검색 API 에 대해서 v1, v2 API 각각 성능테스트**
    - nGrinder 를 이용해 v1, v2 API 각각에 대한 성능 테스트 진행
    - v1, v2 API 에 대한 성능테스트 보고서를 산출물로 작성할 것
    
### 3️⃣  심화 구현 기능
- [ ]  **Cache Eviction 을 이용해 캐시 데이터 동기화 문제 해결하기**
   

## 환경설정
Language : Kotlin  
IDEA : IntelliJ  
JDK : 17.0.10  
Database : super base&postgre sql  
springframework.boot : 3.3.0
