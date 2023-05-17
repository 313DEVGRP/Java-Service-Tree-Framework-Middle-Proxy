# Java-Service-Tree-Framework-All-In-One

![스프링클라우드 구조 확장](https://user-images.githubusercontent.com/17264665/137580215-bcd404a2-90c7-40dd-91d9-48a58ffffccf.png)

* Spring Security 와 Keycloak 을 연동한 인증 인가 시스템 적용 ( Spring security 설정과 동작을 분리 )
* Spring OAuth2RestTemplate 을 활용한 연관 시스템간의 통신 적용 완료 ( 사용자 베이스 통신 동작을 분리 )
* Docker file 을 자동으로 구성하도록 설정
* Artifact Version 을 자동으로 구성하도록 설정
* ZuulProxy 를 활용한 MSA Gateway 구성
* Zipkin 을 활용한 Request Flow 추적

### Those who support us! ###
0. Nexus 의 Realms 에 Docker Bearer Token 을 Active 해야 Docker login 가능 함.
1. 빌드 시, Jenkins agent 가 동작하는 HOST 에 Docker Login {Nexus}를 해야만 401 없이 push 됨