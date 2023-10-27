# 👫 Secondhand

<p align="center"><img src="https://github.com/masters2023-2nd-project-05/second-hand/assets/107349637/a9b41627-4e9c-4efd-8435-3366f50e17e8" width=400></p>

## 프로젝트 소개
>  진행기간 : 2023. 06 ~ 2023. 08

//TODO 간단한 프로젝트 설명과 동기

| 이린 | Dewey |
|:--:|:--:|
|<img src="https://avatars.githubusercontent.com/u/103120173?v=4" alt="이린" width="120" height="120">|<img src="https://avatars.githubusercontent.com/u/115435482?v=4" alt="듀이" width="120" height="120">|

---
## 프로젝트 구조
```java
src
├── main
│   ├── java.com.team5.secondhand
│   │    ├── api    // 외부 API
│   │    │   └── s3 // 이미지 호스팅
│   │    ├── application    // 기본 API
│   │    │   ├── chatroom   // 채팅방 관련 패키지
│   │    │   ├── item       // 판매 상품 관련 패키지
│   │    │   ├── member     // 회원 관련 로패키지직
│   │    │   ├── oauth      // oauth 관련 패키지
│   │    │   ├── region     // 지역 관련 패키지
│   │    │   ├── resource   // 초기 데이터 반환 패키지
│   │    │   └── wishlist   // 좋아요 표시한 상품 관련 패키지
│   │    ├── chat              // 채팅 API
│   │    │   ├── bubble        // 채팅메시지
│   │    │   ├── chatroom      // 채팅metainfo
│   │    │   └── notification  // 채팅 알람
│   │    └── global
│   │        ├── auth
│   │        ├── config
│   │        ├── exception
│   │        ├── model
│   │        ├── properties
│   │        └── util
│   └── resources
└── test
    └── java.com.team5.secondhand
        ├── integration    // 통합테스트
        └── unit           // 단위테스트
```

---
## 구성도
### 인프라 구성
![image](https://github.com/masters2023-2nd-project-05/second-hand-BE/assets/103120173/9e6093ce-7618-411d-8ddf-738070a21865)

### RDB 구성
//TODO 듀이

---
## API 엔드포인트 목록 및 사용법
//TODO 새힘

---
## 주요 기능
### 기능1. 회원가입/로그인
- 내용 : oauth, 중간에 세션에 저장하여 기본 지역을 입력받은 후 회원가입이 완료되는 로직
// 다이어그램
// 기능 설명 조금 (2~3줄)

### 기능2. 실시간 채팅 및 알람 구조
// 다이어그램
// 기능 설명 조금
// 구성
// 1. 채팅방과 채팅 내용 저장 구조
// 2. 채팅 메시지 전달 구조
// 3. 채팅 알람 구조

### 기능3. 


---
## 🧑🏻‍💻 기술스택
![Java](https://img.shields.io/badge/-Java-007396?style=flat&logo=Java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/-Spring_Boot-6DB33F?style=flat&logo=Spring-Boot&logoColor=white)
![SpringDataJPA](https://img.shields.io/badge/-Spring_Data_JPA-6DB33F?style=flat&logo=Spring&logoColor=white)
![MySQL](https://img.shields.io/badge/-MySQL-4479A1?style=flat&logo=MySQL&logoColor=white)   
![AWS EC2](https://img.shields.io/badge/-AWS_EC2-232F3E?style=flat&logo=Amazon-AWS&logoColor=white)
![AWS S3](https://img.shields.io/badge/-AWS_S3-569A31?style=flat&logo=Amazon-S3&logoColor=white)
![AWS RDS](https://img.shields.io/badge/-AWS_RDS-232F3E?style=flat&logo=Amazon-AWS&logoColor=white)
![Docker](https://img.shields.io/badge/-Docker-2496ED?style=flat&logo=Docker&logoColor=white)
![NginX](https://img.shields.io/badge/-NginX-269539?style=flat&logo=Nginx&logoColor=white)
![Github Action](https://img.shields.io/badge/-Github_Action-2088FF?style=flat&logo=Github-Action&logoColor=white)
