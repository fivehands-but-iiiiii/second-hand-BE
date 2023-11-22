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
## API 엔드포인트 목록
- 자세한 내용은 문서를 참고하세요. [링크](https://documenter.getpostman.com/view/27928775/2s9Y5Wy4Jc)

<details><summary>계정 및 인증 (Authentication and Account)</summary>
 
| 설명                                     | HTTP 메서드 | 엔드포인트                    |
|------------------------------------------|-------------|------------------------------|
| 사용자는 로그아웃 할 수 있다.             | GET         | /logout                      |
| 사용자는 OAuth를 통해 로그인 할 수 있다.  | GET         | /git/login?code={string}&env={PROD} |
| 사용자는 회원가입을 할 수 있다 (Github/일반 가입). | POST | /join |
| 사용자는 아이디 중복확인을 할 수 있다.     | GET         | /join/availability?memberId={memberId} |
| 사용자는 로그인을 할 수 있다.             | POST        | /login                       |
| 사용자는 프로필 사진을 설정할 수 있다.    | PATCH       | /members/image               |

</details>

<details><summary>상품 관리 (Item Management)</summary>

| 설명                                       | HTTP 메서드 | 엔드포인트                             |
|--------------------------------------------|-------------|---------------------------------------|
| 사용자는 새로운 상품을 등록할 수 있다.    | POST        | /items                               |
| 판매자는 상품 정보를 수정할 수 있다.     | PUT         | /items/{id}                          |
| 사용자는 상품의 상세 정보를 볼 수 있다.  | GET         | /items/{id}                          |
| 판매자는 판매중인 판매 상품 목록에서 글을 삭제할 수 있다. | DELETE | /items/{id}                          |
| 판매자는 상품 판매 상태만 별도로 수정할 수 있다. | PATCH | /items/{id}/status                  |
| 사용자는 자신의 동네의 상품 목록을 볼 수 있다. | GET | /items?page={number}&region={id}    |
| 사용자는 자신의 동네의 상품 목록을 특정 카테고리별로 볼 수 있다. | GET | /items?page={number}&region={id}&category={id} |
| 사용자는 자신의 동네의 판매중인 카테고리 목록을 볼 수 있다. | GET | /items/categories?regionId={id}   |
| 사용자는 상품 이미지를 첨부할 수 있다.    | POST        | /items/image                         |
| 사용자는 자신이 판매완료한 상품 목록을 볼 수 있다. | GET | /items/mine?page={number}&isSales={boolean} |
| 사용자는 자신이 판매중인 상품 목록을 볼 수 있다. | GET | /items/mine?page={number}&isSales={boolean} |

</details>

<details><summary>채팅 (Chat)</summary>

| 설명                                       | HTTP 메서드 | 엔드포인트                            |
|--------------------------------------------|-------------|--------------------------------------|
| 구매자는 판매자에게 판매상품에 대한 채팅을 생성할 수 있다. | POST | /chats                           |
| 사용자는 나의 채팅 내역을 모두 볼 수 있다. | GET | /chats?page={number}&itemId={number} |
| 사용자는 아이템 상세보기에서 채팅방에 입장할 수 있다. | GET | /chats/items/{itemId}             |
| 사용자는 나의 채팅 내역의 알림을 받을 수 있다. | GET | /chats/subscribe                   |
| 사용자는 채팅방에서 나갈 수 있다 (삭제). | DELETE | /chats/{chatId}                  |
| 사용자는 대화하던 채팅방에 다시 입장할 수 있다. | GET | /chats/{chatId}                   |
| 사용자는 아이템에 대한 이전 채팅 로그를 알 수 있다. | GET | /chats/{chatId}/logs?page={number} |

</details>

<details><summary>실시간 채팅 (Live Chat)</summary>

- 웹소켓 프로토콜입니다.

| 설명                            | 엔드포인트                     |
|-------------------------------|------------------------------|
| 사용자는 채팅 소켓을 연결할 수 있다. | /chat                          |
| 사용자는 채팅방에 입장할 수 있다. (subscribe) | /sub/{chatroomId} |
| 사용자는 채팅방에 메시지를 보낼 수 있다. (publish) | /pub/message             |
 
</details>

<details><summary>상품 관심 등록 (Wishlist)</summary>

| 설명                                       | HTTP 메서드 | 엔드포인트                             |
|--------------------------------------------|-------------|---------------------------------------|
| 사용자는 관심상품으로 등록한 글의 목록 전체를 볼 수 있다. | GET | /wishlist?page={number} |
| 사용자는 관심상품으로 등록한 글의 목록을 카테고리별로 볼 수 있다. | GET | /wishlist?page={number}&category={id} |
| 사용자는 관심상품으로 등록한 아이템의 카테고리 목록을 볼 수 있다. | GET | /wishlist/categories |
| 사용자는 상품을 관심상품으로 등록할 수 있다 (좋아요 누르기). | POST | /wishlist/like |
| 사용자는 관심상품을 관심상품 목록에서 삭제할 수 있다 (좋아요 해제). | DELETE | /wishlist/like?itemId={number} |

</details>


<details><summary>리소스 및 기타 (Resources and etc...)</summary>

| 설명                                       | HTTP 메서드 | 엔드포인트                             |
|--------------------------------------------|-------------|---------------------------------------|
| 사용자는 동네 명단을 볼 수 있다. | GET | /regions?address={string} |
| 초기 데이터를 받을 수 있다. | GET | /resources |
| 사용자는 카테고리 리소스 정보를 받을 수 있다. | GET | /resources/categories |

 </details>

---
## 주요 기능
### 기능1. OAuth2.0을 적용한 회원가입 및 회원 인증 절차
- 사용자 데이터에 대한 보안과 편의성을 위해 OAuth 2.0을 사용하여 회원가입을 할 수 있도록 구현하였습니다.
- OAuth 서버를 통해 사용자 인증을 완료한 후, 잠시 session에 이 데이터를 저장합니다. 사용자로부터 추가로 필요한 지역 정보를 입력받아 회원가입을 완료하고 이 정보를 데이터베이스에 저장합니다.
- OAuth 서버 인증 이후 유효시간 30분 이후에 추가 정보 입력을 시도할 경우, 예외가 발생하여 처음부터 다시 처음부터 가입 절차를 거쳐야 합니다.

#### OAuth 회원가입 및 로그인 Sequence Diagram
```mermaid
sequenceDiagram
    participant User as 사용자
    participant Client as Web server
    participant Server as WAS
    participant OAuthServer as OAuth 서버

    User ->> Client: 인증 요청
    Client ->> User: 리다이렉트 URL 생성
    User -->> Client: 사용자 리다이렉션
    Client ->> OAuthServer: 사용자 인증 및 권한 부여 요청
    OAuthServer -->> Client: 사용자 인증 및 권한 부여 확인
    Client ->> Server: 회원가입 요청
    activate Server
    Server ->> OAuthServer: 회원 정보 요청
    OAuthServer -->> Server: 회원 정보 반환
    Server -->> Server: Server Session에 회원 정보 임시 저장
    Server -->> Client: 추가 지역 정보 필요 요청
    deactivate Server
    Client ->> User: 지역 정보 입력 폼 표시
    User -->> Client: 지역 정보 제출
    Client ->> Server: 지역 정보와 SID 전달
    activate Server
    Server -->> Server: 회원가입 로직 처리
    Server -->> Client: 회원가입 완료 응답
    deactivate Server
    Client -->> User: 로그인 페이지로 리다이렉션
    User ->> Client: 로그인 요청
    Client ->> Server: 로그인 요청
    activate Server
    Server -->> Server: Jwt 토큰 발급
    Server -->> Client: 로그인 완료 및 토큰 응답
    deactivate Server
    Client -->> User: 로그인 완료 및 토큰 응답
```

#### 사용자 권한이 필요한 요청을 했을 때 Sequence Diagram
- 사용자 권한 이 필요한 요청을 했을 때 다음과 같이 요청마다 사용자 정보를 `Authorization` header에서 추출하여 사용합니다.

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant Filter as Servlet Filter
    participant Jwt as JwtUtil
    participant Context as Authorization Context
    participant Controller as Controller
    participant Service as Service

    Client ->> Filter: 보호된 페이지 요청
    Filter ->> Jwt: Authorization 헤더에서 토큰 추출
    activate Jwt
    Jwt -->> Jwt: 토큰 유효성 검증
    Jwt -->> Jwt: 클레임 추출
    Jwt -->> Filter: 클레임 전달
    deactivate Jwt
    Filter ->> Context: 클레임 저장
    Context -->> Controller: 사용자 정보 및 권한 추출
    Controller ->> Service: 요청 전달
    activate Service
    Service -->> Controller: 응답 반환
    deactivate Service
    Controller -->> Filter: 응답 반환
    Filter -->> Client: 응답 반환
```

---

### 기능2. 실시간 채팅 및 알람 구조
- 실시간 채팅을 하기 위해서 STOMP 프로토콜과 Redis Pub/Sub 을 활용하여 채팅 기능을 구현하였습니다.
- CONNECT 요청마다 사용자 정보를 `Authorization` header에서 추출하여 사용합니다.
- Web Socket 연결 중에 예외 발생 시, 해당 연결은 Disconnect 되고 처음부터 다시 연결 시도를 해야합니다. (예외 발생 경우 몇 개 있는데 다 적어야하나? stomp 구조에 맞지 않은 요청이 들어오거나, 인증되지 않은 사용자거나, 경로가 잘못되거나 등등)
- 영구적인 저장이 필요없는 채팅방 참여자 상태 정보는 Redis 에 캐시로 저장되고, 영구적인 저장이 필요한 채팅방 정보와 메시지는 데이터베이스에 저장합니다.

#### 채팅 흐름 Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant WebServer
    participant WAS
    participant Redis
    participant Database

    WAS->>Redis: 채팅 topic 구독
    User->>WebServer: 판매글에서 채팅하기 클릭
    WebServer->>WAS: 채팅방 진입 요청
    WAS->>Database: 채팅방 정보 조회
    opt 채팅방이 생성되지 않은 경우
        WAS->>WAS: 채팅방 생성
        WAS->>Database: 채팅방 정보 저장
        WAS->>Redis: 채팅방 참여자 메타정보 저장
    end
    Database-->>WAS: 채탕방 정보 반환
    WAS-->>WebServer: 채팅방 정보 반환
    WebServer->>WAS: 채팅방 Connect 요청
    WAS-->>WAS: Connect 검증
    WAS-->>Redis: 세션 스토리지에 사용자 세션 정보 저장
    WebServer->>WAS: Subscribe (채팅방 ID) 요청
    WAS-->>WAS: Subscribe (채팅방 ID) 검증
    WAS-->>Redis: 채팅방 참여자 메타정보 갱신
    loop 사용자 채팅
        User->>WebServer: 채팅 메시지 입력 및 보내기
        WebServer->>WAS: Send 요청
        WAS->>+Redis: 채팅 메시지 수신 및 채팅 topic으로 발행
        WAS->>Database: 채팅 내용 저장
        Redis-->>-WAS: 채팅 topic을 구독한 서버로 브로드캐스트
        WAS-->>WebServer: 채팅방에 존재하는 사용자에게 메시지 전달
        WebServer->>User: 채팅 메시지 업데이트
    end
    User->>WebServer: 채팅방 접속 종료
    WebServer->>WAS: Unsubscribe 요청 
    WAS-->>WAS: Unsubscribe 검증
    WAS-->>Redis: 채팅방 참여자 메타정보 갱신
    WebServer-->>WAS: Disconnect 요청
    WAS-->>Redis: 세션 스토리지에서 사용자 세션 정보 삭제 
    loop TTL주기 마다
        Redis-->>Database: 채팅 메타 정보 저장
    end
```

#### 채팅 알람 Sequence Diagram

- 사용자가 WebSocket 연결이 끊겨도 채팅 알람을 받기 위해 SSE를 활용하여 채팅 알람 기능을 구현하였습니다.
```mermaid
sequenceDiagram
    participant User
    participant WebServer
    participant WAS
    participant Redis
    
    User->> WebServer: 로그인
    loop
        WebServer->>+WAS: SSE 연결 요청
        WAS-->>WebServer: SSE 연결 승인
        loop 
            User->> WebServer: 채팅 메시지 보내기
            WebServer->>+WAS: 채팅 메시지 전송
            WAS->>Redis: 메시지 저장
            WAS->>-WebServer: SSE를 통한 메시지 알림 전송
        end
        alt SSE 타임아웃일 경우
            WebServer->>WAS: SSE 연결 재요청
            WAS-->>WebServer: SSE 연결 승인
        end
        WebServer->>WAS: SSE 연결 종료 요청
        WAS-->>-WebServer: SSE 연결 종료 응답
    end
```

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
