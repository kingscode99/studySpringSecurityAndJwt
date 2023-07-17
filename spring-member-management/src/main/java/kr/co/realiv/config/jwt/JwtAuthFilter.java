package kr.co.realiv.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import kr.co.realiv.config.exception.ErrorCode;
import kr.co.realiv.data.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
//OncePerRequestFilter abstract class 를 구현하는 JwtAuthFilter 생성.
//이 class는 단지 필터 일 뿐 검증 로직은 JwtUtil 에 구현 되어 있음.
public class JwtAuthFilter extends OncePerRequestFilter {

    //JwtUtil Bean 등록.
    private final JwtUtil jwtUtil;

    //OncePerRequestFilter 에서 제공하는 doFilter method 구현.
    //HttpServletRequest와 HttpServletResponse, FilterChain 을 매개 변수로 받아옴.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //token은 JwtUil 의 resolveToken에 Requset를 넘겨서 받아올것.
        String token = jwtUtil.resolveToken(request);
        //token 이 null이 아닐 때
        if (token != null) {
            //token 이 유효한지 검사하는 .validateToken() method
            if (!jwtUtil.validateToken(token)) {
                //아래에서 설명.
                setErrorResponse(response, ErrorCode.NOT_VALIDATE_TOKEN);
                return;
            }
            //token 이 검증이 완료 되었으면 권한 세팅을 하고(user정보를 token에서 받아온 이후 권한 설정.)
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        //token 이 null이든 null이지 않든 filterChain에 한번 더 거름.
        filterChain.doFilter(request, response);
    }

    //filter 는 따로 exception 을 처리 하지 않으면 OncePerRequestFilter 로 인해 무한 루프를 돌다가 error toManyProcess 를 던지기 때문에
    //exception 처리를 따로 해줌.
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        //출력하는 타입은 map 타입. ObjectMapper 생성.
        ObjectMapper objectMapper = new ObjectMapper();
        //status 는 errorCode 의 HttpStatus.val();
        response.setStatus(errorCode.getHttpStatus().value());
        //미디어 타입은 json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //인코딩 utf8
        response.setCharacterEncoding("utf-8");
        //errorMessage 를 "data" 값으로 하는 ResponseDto 객제 생성.
        ResponseDto responseDto = new ResponseDto(errorCode.getErrorMessage());
        //response 출력.
        try {
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //jwtUtil에서 만든 권한을 security에 set.
    private void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}
