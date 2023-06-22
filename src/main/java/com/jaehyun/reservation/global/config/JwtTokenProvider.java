package com.jaehyun.reservation.global.config;

import com.jaehyun.reservation.global.service.CustomUserDetailService;
import com.jaehyun.reservation.user.type.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    //환경변수에 키값 지정
    @Value("${spring.jwt.secret}")
    private String secretKey;

    public static final String TOKEN_HEADER = "Authorization"; //토큰헤더
    public static final String TOKEN_PREFIX = "Bearer "; //jwt토큰

    // 토큰 유효시간 1시간
    private long tokenValidTime = 1000 * 60 * 60; //1시간(1000 * 60 * 60)

    private final CustomUserDetailService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userPk, RoleType roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰 유효기간
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 사용할 암호화 알고리즘
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "Bearer +TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader((TOKEN_HEADER));

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) { //토큰형태 포함
            return token.substring(TOKEN_PREFIX.length()); //실제토큰 부위
        }

        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}