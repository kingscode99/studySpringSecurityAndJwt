package kr.co.realiv.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.co.realiv.config.exception.CheckApiException;
import kr.co.realiv.config.exception.ErrorCode;
import kr.co.realiv.config.security.UserDetailsServiceImpl;
import kr.co.realiv.data.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    //user 객체를 db에서 가져오기 위한 UserDetailsServiceImpl
    private final UserDetailsServiceImpl userDetailsService;

    //Header 에 "Authorization" 란 값으로 토큰을 보낼 예정.
    public static final String AUTHORIZATION_HEADER = "Authorization";

    //토큰에 "auth"라는 값으로 Role 을 저장할 예정.
    public static final String AUTHORIZATION_KEY = "auth";

    //토큰 앞에 Bearer 토큰이라는 것을 명시해 주기 위한 문자열 "Bearer "을 붙일 예정.
    public static final String BEARER_PREFIX = "Bearer ";

    //시간은 60분으로 설정.
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    //secretKey 는 realiv를 sha256으로 encode 한 값.
    @Value("${jwt.secret.key}")
    private String secretKey;

    //Key class(Security 제공.)
    private Key key;

    //토큰 encode 알고리즘은 HS256으로 할것.
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //key 값을 설정 할건데 이 값이 없으면 token 의 값을 변경 할 수 없음.(보안)
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //token 생성
    public String createToken(String userId, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        //"sub" 값에 id 값이 들어감.
                        .setSubject(userId)
                        //"Authorization" 칸 안에 role 값이 들어감 (https://jwt.io/) 에서 토큰 decode 가능.
                        .claim(AUTHORIZATION_KEY, role)
                        //token time 설정.
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        //알고리즘과 key 값 설정.
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    //앞서 만들었던 토큰은 Bearer + "token" 형식이기 때문에 request 에서 받아온 정보에서 "Bearer " 을 제외 시켜야 해서 일단
    //받아온 토큰이 bearer토큰 형식인지 확인 후 "Bearer " 문자열이 포함 되어 있다면 substring으로 날려버림.
    //이후 토큰은 "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3eWo5OSIsImF1dGgiOiJBRE1JTiIsImV4cCI6MTY4OTMwMTA5NywiaWF0IjoxNjg5Mjk3NDk3fQ.7J5UN1Hl8ftG_Pl4uMP6V7E0Fe9939L6zVA2fMgZRcY"
    //와 같은 형식이 됨.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //토큰 유효성 검사.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT token 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원 되지 않는 JWT token 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT token 입니다.");
        }
        return false;
    }

    //이전에 만들었던 secretKey를 사용하여 token에서 body를 가져옴.
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //권한 만들기.
    public Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
