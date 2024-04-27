# 독서록 공유 커뮤니티 'Wead'
자신이 읽은 책에 대하여 독서록을 작성하고 공유하는 커뮤니티

## 사이트 사진
<img width="1278" alt="wead_screenshot" src="https://github.com/sangwon0202/wead/assets/146148180/0932ab0a-5aea-4680-baad-3f728a3973f5"></img>

## 사용한 기술
* Java 17
* Spring boot 3.2.3, Spring Web MVC
* MySQL, Spirng Data JPA, Redis
* Thymeleaf

## 실행 방법

### 1. git clone 하기
```
git clone https://github.com/sangwon0202/wead
```
### 2. application-private.yml 작성하기
src/main/resources 폴더에 application-private.yml를 아래와 같이 작성한다.
```yml
spring:
  datasource:
    url: {DB 주소}
    username: {DB 아이디}
    password: {DB 비밀번호}

  data:
    redis:
      host: {Redis 주소}

naverAPI:
  client:
    id: {Naver API 아이디}
    password: {Naver API 비밀번호}
```
### 3-1. build 및 실행하기
```
./gradlew build
java -jar build/libs/wead.jar
```
### 3-2. Docker 컨테이너로 만들기
```
docker build
```

## Trouble Shooting
* interceptor 자동 등록 구현
* fetch join과 서브 쿼리를 이용한 DB I/O 최적화
* 람다 함수를 이용한 공통 로직 분리
* 외부 API 호출을 최소화하기 위해 Redis를 이용한 캐시 구현







  

  
  
  

