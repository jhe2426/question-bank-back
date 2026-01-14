package com.jhe.question_bank.filter;

import java.io.IOException;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jhe.question_bank.provider.JwtProvider;
import com.jhe.question_bank.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        try {

            String token = getToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String userId = jwtProvider.validateAccessToken(token);
            if (userId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            boolean existUser = userRepository.existsByUserId(userId);
            if (!existUser) {
                filterChain.doFilter(request, response);
                return;
            }

            setContext(userId, request);
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        filterChain.doFilter(request, response);

    }
    
    // function: Request 객체에서 Token 추출 메서드 //
    private String getToken(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        boolean hasAuthorization = StringUtils.hasText(authorization);
        if (!hasAuthorization) return null;

        boolean isBearer = authorization.startsWith("Bearer ");
        if (!isBearer) return null;

        String token = authorization.substring(7);
        return token;

    }


    // function: Security Context 생성 및 등록 //
    private void setContext(String userId, HttpServletRequest request) {

        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(securityContext);

    }
}
 