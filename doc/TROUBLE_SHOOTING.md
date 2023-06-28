### TROUBLE SHOOTING

- <b>문제</b> 발생일시, 문제점, 해결법을 적는 공간.

#### 01
- 발생일시 20230624 23:00 ~ 20230625 03:30

- 문제점
  - Jwt 토큰의 Role을 ListType > EnumType 으로 바꾸면서</br>
  UserDetails 를 implement 받는 JwtUserDetails 의 getAuthorities 가 정상 작동하지 않았음.</br>
  - doFilter 를 타기 전에 권한이 USER 인 것을 확인하였지만 그 이후 권한이 필요한 모든 요청이 403 Forbidden Error 발생

- 해결법
  -     authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRoles().getKey()));
  - user.getRoles().getKey() 로는 해결되지 않았으며, 상단 코드의 "ROLE_" 를 추가로 작성하니 작동.
  - "ROLE_" 를 자동으로 spring 에서 주입해준다는 stackoverflow 글을 보고 저것때문은 아닐것이라 생각하였지만</br>
    "ROLE_" 추가하고 해결. 앞전에 바꾸었던 모든 jwt 세팅 원점으로 돌리고 commit.
  - 참고 링크 https://www.inflearn.com/questions/843578/enum-%ED%83%80%EC%9E%85%EC%9D%98-getauthorities

