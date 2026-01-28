package com.jhe.question_bank.service.implement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.auth.IdCheckRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.auth.AccessTokenRefreshResponseDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;
import com.jhe.question_bank.common.entity.ApprovalCodeEntity;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.provider.JwtProvider;
import com.jhe.question_bank.repository.ApprovalCodeRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.AuthService;
import com.jhe.question_bank.store.RefreshTokenStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final ApprovalCodeRepository approvalCodeRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenStore refreshTokenStore;
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

            ApprovalCodeEntity approvalCodeEntity = approvalCodeRepository.findByUserId(userId);
            if (approvalCodeEntity == null) return ResponseDto.signInFail();

            String expireDate = approvalCodeEntity.getExpireDate();
            LocalDate expiresAt = LocalDate.parse(expireDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            boolean isNotExpired = now.isBefore(expiresAt) || now.isEqual(expiresAt);
            if(!isNotExpired) return ResponseDto.authorizationCodeExpired();

            accessToken = jwtProvider.createAccessToken(userId);
            refreshToken = jwtProvider.createRefreshToken(userId);
            csrfToken = UUID.randomUUID().toString();

            refreshTokenStore.save(userId, refreshToken);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignInResponseDto.success(accessToken, refreshToken, csrfToken);
    }

    @Override
    public ResponseEntity<? super AccessTokenRefreshResponseDto> refreshAccessToken(String refreshToken) {
        
        String accessToken = null;

        try {

            String userId = jwtProvider.validateRefreshToken(refreshToken);
            if (userId == null) return ResponseDto.authenticationFail();

            boolean isMatched = refreshTokenStore.matches(userId, refreshToken);
            if (!isMatched) return ResponseDto.authenticationFail();

            boolean existUser = userRepository.existsById(userId);
            if (!existUser) return ResponseDto.authenticationFail();

            accessToken = jwtProvider.createAccessToken(userId);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.serverError();
        }
        return AccessTokenRefreshResponseDto.success(accessToken);
    }

    @Override
    public ResponseEntity<ResponseDto> idCheck(IdCheckRequestDto dto) {
        try {    
            String userId = dto.getUserId();
            boolean isExistUserId = userRepository.existsByUserId(userId);
            if (isExistUserId) return ResponseDto.duplicatedUserId();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseDto> logout(String userId) {
        try {

            refreshTokenStore.delete(userId);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.serverError();
        }
        
        return ResponseDto.success(HttpStatus.OK);
    }
    
}
