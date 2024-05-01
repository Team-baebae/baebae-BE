# 💻 시스템 아키텍처

![image](https://github.com/Team-baebae/baebae-BE/assets/59834576/3bc9448e-0afc-4b90-a235-36037720e934)



# 💻 기술 스택

#### 📀 Back-end :  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-3DDC84?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)
[![JPA](https://img.shields.io/badge/jpa-007396?style=for-the-badge&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![QueryDSL](https://img.shields.io/badge/QueryDSL-3399FF?style=for-the-badge&logoColor=white)](https://github.com/querydsl/querydsl)
[![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)](https://jwt.io/)
[![OAuth2](https://img.shields.io/badge/OAuth2-0088FF?style=for-the-badge&logo=OAuth&logoColor=white)](https://oauth.net/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)

#### 💾 Infra :  
[![Server](https://img.shields.io/badge/Server-0B84FF?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/)
[![Load Balancer](https://img.shields.io/badge/Load%20Balancer-FF6B00?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/)
[![Certificate Manager](https://img.shields.io/badge/Certificate%20Manager-EB144C?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/)
[![DNS](https://img.shields.io/badge/DNS-34B900?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/)
[![VPC](https://img.shields.io/badge/VPC-FFB43B?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/)
[![Auto Scaling](https://img.shields.io/badge/Auto%20Scaling-4C00FF?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/)
[![Object Storage](https://img.shields.io/badge/Object%20Storage-FF5733?style=for-the-badge&logoColor=white)](https://en.wikipedia.org/wiki/Object_storage)
[![Naver Clova](https://img.shields.io/badge/Naver%20Clova-38A3A5?style=for-the-badge&logo=naver&logoColor=white)](https://clova.ai/)
[![AWS Lambda](https://img.shields.io/badge/AWS%20Lambda-F7975A?style=for-the-badge&logo=amazon-aws&logoColor=white)](https://aws.amazon.com/lambda/)
[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Nat Gateway](https://img.shields.io/badge/Nat%20Gateway-FFA500?style=for-the-badge&logoColor=white)](https://www.ncloud.com/)
[![nGrinder](https://img.shields.io/badge/nGrinder-19A974?style=for-the-badge&logoColor=white)](https://naver.github.io/ngrinder/)


#### 💾DB : 
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![H2](https://img.shields.io/badge/H2-004080?style=for-the-badge&logoColor=white)](https://www.h2database.com/html/main.html)

#### 🚀 CI/CD :   
[![GitHub Action](https://img.shields.io/badge/GitHub%20Action-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)](https://github.com/features/actions)
[![Source Deploy](https://img.shields.io/badge/Source%20Deploy-6E5494?style=for-the-badge&logoColor=white)](https://en.wikipedia.org/wiki/Deployment)
[![Naver Agent](https://img.shields.io/badge/Naver%20Agent-00C3FF?style=for-the-badge&logoColor=white)](https://www.naver.com/) [![Object Storage](https://img.shields.io/badge/Object%20Storage-FF5733?style=for-the-badge&logoColor=white)](https://en.wikipedia.org/wiki/Object_storage)

<br>
<br>

# 기술스택 선정 이유

### 📀 Back-end

### __```Spring Boot```__
  • Java 기반의 애플리케이션을 빠르게 구축할 수 있게 도와주는 프레임워크 <br>
  • 자동 설정, 스타터 패키지, 내장 서버 등을 통해 개발자가 인프라에 신경 쓰지 않고 비즈니스 로직에 집중가능 <br>
  • 웹 애플리케이션의 기본 구조를 구성하는 데 사용

### __```Spring Security```__
  • 인증과 권한 부여를 위한 포괄적인 보안 프레임워크 <br>
  • 애플리케이션의 보안을 강화하고, 사용자 인증 및 권한 관리를 효율적으로 수행 <br>

### __```JPA```__
  • Java ORM 기술로 데이터베이스와 객체 지향 프로그래밍 사이의 "임피던스 불일치"를 해결 <br>
  • 데이터베이스 테이블과 Java 객체 간의 매핑 <br>
  • 데이터베이스 작업을 간소화하고 데이터 관리를 더욱 효율적으로 수행

### __```QueryDSL```__
  • 타입 안전성을 보장하는 SQL과 유사한 문법을 제공하여 복잡한 쿼리를 쉽게 작성할 수 있게 해주는 프레임워크 <br>
  • JPA와 함께 사용될 때, 복잡한 데이터 검색 작업을 보다 쉽게 처리

### __```Gradle```__
  • 빌드 및 의존성 관리 도구로, Maven과 비교하여 더 유연하고 강력한 기능을 제공

### __```Swagger```__
  • API 문서를 자동으로 생성하고, API 엔드포인트를 테스트할 수 있는 인터페이스

### __```JWT```__
  • 사용자 인증에 사용되는 토큰 기반 기술로, 서버와 클라이언트 간의 안전한 정보 교환을 가능 <br>
  • Spring Security와 함께 사용하여 세션 관리 없이 상태를 유지하는 RESTful 서비스를 구현

### __```OAuth2```__
  • 외부 제공자를 통한 인증을 구현하는 데 사용되는 프레임워크 <br>
  • 사용자가 kako 계정으로 로그인

### __```Thymeleaf```__  
  • 서버 사이드 Java 템플릿 엔진으로, HTML에서 서버의 데이터를 렌더링하는 데 사용

### 💾 Infra  

### __```Server```__ 
 • 네이버 클라우드에서 제공하는 클라우드 컴퓨팅 서비스  
 • 서버를 배포하기 위해 사용  
 • Private Subnet에 실서버 구축 및 Public Subnet에 Test Server 구축  
 • Test Server를 통해 테스트 환경 구축 및 Bastion Host 기능 수행

### __```Load Balancer```__
 • HTTP 트래픽을 제어하기 위해 Network 7계층의 Application Load Balancer 사용  
 • AutoScaling 연결로 인한 확장성 증가  
 • 트래픽을 분산 시키므로, 시스템 가용성 증가  
 • 80port로 들어왔을 때, 443port로 리다이렉트

### __```Certificate Manager```__ 
 • SSL 인증서 발급 받기 위해 사용   
 • Load Balancer에 연동하여 HTTPS 프로토콜 적용 

### __```Global DNS```__ 
 • Load Balancer 및 Front 배포 서버에 Domain 이름을 적용시키기 위해 사용

### __```Auto Scaling```__ 
 • 트래픽의 변화에 서버를 탄력적으로 관리 가능  
 • 서버 scale-out 및 scale-in 자동화  
 • Launch Configuration을 서버 환경설정 간소화  
 • Load Balancer 연동을 통한 효과적인 트래픽 분산

### __```VPC```__ 
 • 클라우드 환경에서 사설 네트워크망을 구축해 안정성 확보  
 • ACG 및 Network ACL 이중 방화벽을 통한 보안 강화  
 • 공인 IP 발급을 줄여, 비용 감소  
 • 내부에 Subnet을 나눠 효율적인 인프라 공간 분리  
 • Public Subnet엔 Load Balancer, Private Subnet에 서버 및 DB를 구축함으로써, 외부 침입 방어

### __```Nat GateWay```__ 
 • VPC 내부 Private Subnet의 서버가 하나의 공인 IP를 활용하여 외부 인터넷과 통신할 수 있도록 하는 Gateway  
 • 스프링 서버내에서 발생하는 API 호출할 때 필요  
 • 외부에선 트래픽이 들어올 수 없어 보안 강화  

### __```Object Storage```__ 
 • 사용자 피드의 사진 및 파일이 저장되는 저장소  
 • AWS S3와 연동되어 높은 호환성  
 • 고가용성으로 인한 시스템 안정성 확보

### __```Naver Clova```__ 
 • 질문자들의 랜덤 질문 생성시에 CLOVA AI 활용  
 • 다양한 질문 프롬프트를 통해 선택옵션 다양화


### __```FireBase```__ 
 • 사용자에게 실시간 알림을 보내기 위한 기술  
 • FCM을 통해 사용자에게 푸시 메세지 전송가능  
 • Message Queue 도입을 고민했지만, 유저가 많지 않은 상황에서 Message Queue 도입은 무의미하고, 시스템 불안정성을 야기할 수 있다 판단하여 FireBase도입

### __```nGrinder```__ 
 • 오픈소스 기반 부하테스트 소프트웨어  
 • 트래픽 부하를 발생시켜 AutoScaling Scale-Out 테스트 및 모니터링 진행  
 • API 성능 개선의 목표가 아니기 때문에, 외부에서 직접 부하 테스트 진행   

 ### __```AWS Lambda```__ (이 부분은 아직 미정입니다..!)
 • FireBase와 연동시켜, 사용자에게 실시간 알림 전송  
 • 간단한 환경 구축 및 높은 안정성 확보  
 • 추후 기능 다른 기능 업데이트시, 높은 확장성 확보  

<br>
<br>

### 💾DB

### __```MySQL```__ 
 • 서버와 연결되는 메인 DB

### __```Redis```__ 
 • 로그인 JWT 및 FCM Token 2차 저장소로 활용  
 • Cache처럼 활용하여, token 조회 성능 개선

### __```H2```__ 
 • 테스트 서버에서 간단한 DB 구축용으로 활용

<br>
<br>

### 🚀 CI/CD 

### __```GitHub Action```__ 
 • Github와 연동해 CI-CD 파이프라인 구축  
 • main -> 실서버 배포 CI-CD  
 • develop -> 테스트서버 배포 CI-CD  
 • 그 외 -> Commit & PR시, 자동 빌드 및 테스트 진행

### __```Object Storage```__ 
 • CI 진행 후, Jar 파일을 압축시켜 Object Storage에 저장  
 • 파일을 버전별로 나눌 수 있어, 쉽게 Rollback 가능  
 • AWS S3 CLI와 연동되어, 간편하게 파일 저장 가능

### __```Source Deploy```__ 
 • Object Storage의 파일을 기반으로 AutoScaling CD 진행  
 • 배포 스크립트를 통해 Spring 서버 실행  
 • Auto Scaling Group별로 블루/그린 무중단 배포  
 • GitHub Action에서 naver signiture v2 방식으로 API 호출가능

### __```Naver Agent```__ 
 • Source Deploy를 적용하기 위해 꼭 필요한 프로그램  
 • Server에 자동 스크립트를 적용하여 자동 설치

<br>
<br> 

# 💻 네이밍 룰
☑️ 패키지명 : lowercase  
☑️ 클래스명 : PascalCase  
☑️ 상수명 : UPPERCASE   
☑️ 메소드 및 변수명 : camelCase 
 
<br>

# 💻 커밋 컨벤션

| 이름      | 설명            |
|-----------|-----------------|
| Feat      | 새로운 기능 추가       |
| Fix       | 버그 수정 및 기능 수정  |
| Docs  | 문서 추가 및 수정 |
| Test      | 테스트 코드 추가 및 수정    |
| Refactor     | 코드 리팩토링 |
| Rename     | 파일 및 폴더명 수정 |
| Remove     | 파일 삭제 |
| Chore     | 그 외 자잘한 수정 |

<br>

# 💻 깃 플로우
![image](https://github.com/Team-baebae/baebae-BE/assets/59834576/8abd2da1-bf0d-4056-bb51-dfc2b8833d9b)



