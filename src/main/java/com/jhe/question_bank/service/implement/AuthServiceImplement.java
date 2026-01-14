package com.jhe.question_bank.service.implement;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.provider.JwtProvider;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;
        String csrfToken = null;

        try {

            String userId = dto.getUserId();
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) return ResponseDto.signInFail();

            String userPassword = dto.getUserPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatch = passwordEncoder.matches(userPassword, encodedPassword);
            if (!isMatch) return ResponseDto.signInFail();

            accessToken = jwtProvider.createAccessToken(userId);
            refreshToken = jwtProvider.createRefreshToken(userId);
            csrfToken = UUID.randomUUID().toString();
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignInResponseDto.success(accessToken, refreshToken, csrfToken);
    }
    
}
