# RSS-Reader
파리지옥 템플릿에 사용될 Rss Reader를 만드는 저장소입니다.

---
## 프로젝트 소개
- 배포 URL :
- API Swagger :
- 진행기간 : 2023. 11 ~ 진행 중
- 팀 블로그: 
> RSS-Reader는 사용자가 여러 블로그 플랫폼을 구독하여 원하는 Feed를 볼 수 있고 서로 반응을 나눌 수 있는 플랫폼 서비스입니다.

---
## 팀원 소개
<table>
 <tr>
    <td align="center"><a href="https://github.com/jinny-l"><img src="https://avatars.githubusercontent.com/jinny-l" width="150px;" alt=""></td>
    <td align="center"><a href="https://github.com/new-pow"><img src="https://avatars.githubusercontent.com/new-pow" width="150px;" alt=""></td>
    <td align="center"><a href="https://github.com/crtEvent"><img src="https://avatars.githubusercontent.com/crtEvent" width="150px;" alt=""></td>
    <td align="center"><a href="https://github.com/leegyeongwhan"><img src="https://avatars.githubusercontent.com/leegyeongwhan" width="150px;" alt=""></td>
    <td align="center"><a href="https://github.com/jaea-kim"><img src="https://avatars.githubusercontent.com/jaea-kim" width="150px;" alt=""></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/jinny-l"><b>jinny-l</b></td>
    <td align="center"><a href="https://github.com/new-pow"><b>new-pow</b></td>
    <td align="center"><a href="https://github.com/crtEvent"><b>crtEvent</b></td>
    <td align="center"><a href="https://github.com/leegyeongwhan"><b>leegyeongwhan</b></td>
    <td align="center"><a href="https://github.com/jaea-kim"><b>jaea-kim</b></td>
  </tr>
</table>

<br/>

---

## 기술스택, 및 개발환경
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

- 디자인 :
- 코드 컨벤션 :
- 브랜치 전략 :
- 스크럼 회의록:

---
## 레이어 구조
```
        📂 config
        📂 global
            📂 utill
        📂 domian
         📂 ...{domain_name}
            🟢 {domain_name}.java
        📂 exception
            🟢 GlobalHttpExceptionHandler
            🟢 ErrorObject
            🟢 NoSuchElementException ...
        📂 presentation
            📂 controller
            📂 dto
            📂 docs // swagger custom annotation
        📂 service
        📂 infrastructure
            📂 entity
                📂 ...{domain_name}
                    🟢 {domain_name}Entity.java
        📂 repository
        📂 api
```

---

## 인프라 구조

//topdo: 인프라 구조도 추가

---
## 프로젝트 설치 및 실행 방법
**요구사항**
애플리케이션을 구축하고 실행하려면 다음이 필요합니다.
- Java 17
- Spring Boot 3.0.0 이상

설치
```
$ git clone https://github.com/FlytrapHub/RSS-Reader.git
$ cd rss-reader
```

백엔드 로컬 실행 예시
```
yml 파일 자신에게 맞게 수정 후

./gradlew build

docker build -t {gamja123/boards} .

docker docker-compose up
```
---
## 사용 방법
//todo: 도영상 촬영

---
## 프로젝트 구조
### 주요 서비스 시퀀스 다이어 그램

```mermaid
sequenceDiagram
  participant Schedule
  participant PostCollectService
  participant RssPostParser
  participant RssItemResource
  participant PostEntity
  participant PostEntityJpaRepository

  Schedule ->> PostCollectService: collectPosts(): 10분마다 게시글 수집
  activate PostCollectService
  loop 저장된 구독 블로그 수 만큼 반복
    PostCollectService ->>+ RssPostParser: parseRssDocument(): RSS XML 문서 파싱
    loop 파싱한 블로그 포스트 수 만큼 반복
      RssPostParser ->>+ RssItemResource: <<create>>
      RssItemResource -->>- RssPostParser: 파싱 결과 생성: RssItemResource
    end
    RssPostParser -->>- PostCollectService: 파싱 결과 리스트 생성: List<RssItemResource>
    loop 파싱한 블로그 포스트 수 만큼 반복
      PostCollectService ->>+ PostEntity: <<create>>
      PostEntity -->>- PostCollectService: DB 저장을 위한 엔티티 생성: PostEntity
      PostCollectService ->>+ PostEntityJpaRepository: existsByGuidAndSubscribe(): 파싱한 게시글이 존재하는지 확인
      PostEntityJpaRepository -->>- PostCollectService: true/false 반환
      alt 게시글이 존재하지 않으면
        PostCollectService ->> PostEntityJpaRepository: save(): DB에 저장
      end
    end
  end
  deactivate PostCollectService
```

![img.png](img.png)


---
### 목차
//TODO: 프로젝트의 주요 서비스 구조를 추가
- Scheduler를 통한 Post 크롤링 작업
- CI/CD
---

## 기타

---
