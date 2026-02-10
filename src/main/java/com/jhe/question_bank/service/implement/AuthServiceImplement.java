package com.jhe.question_bank.service.implement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.auth.ApprovalCodeRequestDto;
import com.jhe.question_bank.common.dto.request.auth.ApprovalCodeVerifyRequestDto;
import com.jhe.question_bank.common.dto.request.auth.IdCheckRequestDto;
import com.jhe.question_bank.common.dto.request.auth.PhoneNumberAuthCodeRequestDto;
import com.jhe.question_bank.common.dto.request.auth.PhoneNumberAuthCodeVerifyRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignUpRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.auth.AccessTokenRefreshResponseDto;
import com.jhe.question_bank.common.dto.response.auth.GetUniversitiesResponseDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;
import com.jhe.question_bank.common.entity.ApprovalCodeEntity;
import com.jhe.question_bank.common.entity.UniversityEntity;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.common.util.AuthCodeCreator;
import com.jhe.question_bank.provider.JwtProvider;
import com.jhe.question_bank.provider.SmsProvider;
import com.jhe.question_bank.repository.ApprovalCodeRepository;
import com.jhe.question_bank.repository.UniversityRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.AuthService;
import com.jhe.question_bank.store.PhoneNumberAuthStore;
import com.jhe.question_bank.store.RefreshTokenStore;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final ApprovalCodeRepository approvalCodeRepository;
    private final UniversityRepository universityRepository;

    private final JwtProvider jwtProvider;
    private final SmsProvider smsProvider;

    private final RefreshTokenStore refreshTokenStore;
    private final PhoneNumberAuthStore phoneNumberAuthStore;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;
        String csrfToken = null;

        String userId = dto.getUserId();

        try {

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
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        accessToken = jwtProvider.createAccessToken(userId);
        refreshToken = jwtProvider.createRefreshToken(userId);
        csrfToken = UUID.randomUUID().toString();

        try {

            refreshTokenStore.save(userId, refreshToken);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.redisServerError();
        }

        return SignInResponseDto.success(accessToken, refreshToken, csrfToken);
    }

    @Override
    public ResponseEntity<? super AccessTokenRefreshResponseDto> refreshAccessToken(String refreshToken) {
        
        String userId = jwtProvider.validateRefreshToken(refreshToken);
        if (userId == null) return ResponseDto.authenticationFail();

        try {

            boolean isMatched = refreshTokenStore.matches(userId, refreshToken);
            if (!isMatched) return ResponseDto.authenticationFail();
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.redisServerError();
        }

        try {

            boolean existUser = userRepository.existsById(userId);
            if (!existUser) return ResponseDto.authenticationFail();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        String accessToken = jwtProvider.createAccessToken(userId);

        return AccessTokenRefreshResponseDto.success(accessToken);
    }

    @Override
    public ResponseEntity<? super GetUniversitiesResponseDto> getUniversities() {

        List<UniversityEntity> universityEntities = new ArrayList<>();

        try {
            
            universityEntities = universityRepository.findAllByOrderByUniversityIdAsc();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        
        return GetUniversitiesResponseDto.success(universityEntities);
    }

    @Override
    public ResponseEntity<ResponseDto> idCheck(IdCheckRequestDto dto) {

        String userId = dto.getUserId();

        try {    
            boolean isExistUserId = userRepository.existsByUserId(userId);
            if (isExistUserId) return ResponseDto.existsUserId();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseDto> phoneNumberAuthCode(PhoneNumberAuthCodeRequestDto dto) {
        
        String phoneNumber = dto.getPhoneNumber();

        try {
            
            boolean isExistedPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
            if (isExistedPhoneNumber) return ResponseDto.existsUserPhoneNumber();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        try {

            boolean isAuthCodeIssued = phoneNumberAuthStore.authCodeExists(phoneNumber);
            if (isAuthCodeIssued) return ResponseDto.authCodeAlreadySent();
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.redisServerError();
        }

        String authCode = AuthCodeCreator.generatePhoneNumberAuthCode();

        boolean isSendSuccessful = smsProvider.sendPhoneNumberAuthCodeMessage(phoneNumber, authCode);
        if (!isSendSuccessful) return ResponseDto.smsSendFail();

        try {

            phoneNumberAuthStore.saveAuthCode(phoneNumber, authCode);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.redisServerError();
        }
        
        return ResponseDto.success(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDto> phoneNumberAuthCodeVerify(PhoneNumberAuthCodeVerifyRequestDto dto) {

        String phoneNumber = dto.getPhoneNumber();
        String authCode = dto.getAuthCode();
        
        try {
            boolean isAuthCodeValid = phoneNumberAuthStore.isAuthCodeValid(phoneNumber, authCode);
            if (!isAuthCodeValid) return ResponseDto.phoneNumberAuthFail();

            phoneNumberAuthStore.verifySave(phoneNumber);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.redisServerError();
        }

        return ResponseDto.success(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseDto> approvalCode(ApprovalCodeRequestDto dto) {
        String phoneNumber = dto.getPhoneNumber();

        String approvalCode = AuthCodeCreator.generateApprovalCode();

        boolean isSendSuccessful = smsProvider.sendApprovalCodeMessage(phoneNumber, approvalCode);
        if (!isSendSuccessful) return ResponseDto.smsSendFail();

        try {
            
            ApprovalCodeEntity approvalCodeEntity = new ApprovalCodeEntity(approvalCode);
            approvalCodeRepository.save(approvalCodeEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success(HttpStatus.CREATED);
    }

    

    @Override
    public ResponseEntity<ResponseDto> approvalCodeVerify(ApprovalCodeVerifyRequestDto dto) {
        
        String userId = dto.getUserId();
        String approvalCode = dto.getApprovalCode();

        try {
            
            boolean isExistedUserId = userRepository.existsByUserId(userId);
            if (isExistedUserId) return ResponseDto.existsUserId();

            ApprovalCodeEntity approvalCodeEntity = approvalCodeRepository.findByApprovalCode(approvalCode);
            if (approvalCodeEntity == null) return ResponseDto.approvalCodeAuthFail();
            
            String approvalCodeExpireDate = approvalCodeEntity.getExpireDate();
            boolean isExistedApprovalCodeExpireDate = approvalCodeExpireDate != null;
            if (isExistedApprovalCodeExpireDate) return ResponseDto.usedApprovalCode();

            ApprovalCodeEntity occupyingUserIdApprovalCodeEntity = approvalCodeRepository.findByUserId(userId);
            if (occupyingUserIdApprovalCodeEntity != null) {
                String occupyingApprovalCode = occupyingUserIdApprovalCodeEntity.getApprovalCode();
                if (!occupyingApprovalCode.equals(approvalCode)) {
                    occupyingUserIdApprovalCodeEntity.updateUserId(null);
                    approvalCodeRepository.saveAndFlush(occupyingUserIdApprovalCodeEntity);
                }
            }

            approvalCodeEntity.updateUserId(userId);
            approvalCodeRepository.save(approvalCodeEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success(HttpStatus.OK);
    }

    

    @Transactional
    @Override
    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto) {

        String userId = dto.getUserId();
        String phoneNumber = dto.getPhoneNumber();
        String approvalCode = dto.getApprovalCode();
        String password = dto.getPassword();

        try {

            boolean isExistUserId = userRepository.existsByUserId(userId);
            if (isExistUserId) return ResponseDto.existsUserId();

            boolean isExistedPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
            if (isExistedPhoneNumber) return ResponseDto.existsUserPhoneNumber();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        try {
            
            boolean isPhoneAuthVerified = phoneNumberAuthStore.isVerified(phoneNumber);
            if (!isPhoneAuthVerified) return ResponseDto.phoneNumberAuthFail();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.redisServerError();
        }

        try {
            
            ApprovalCodeEntity approvalCodeEntity = approvalCodeRepository.findByApprovalCodeAndUserId(approvalCode, userId);
            if (approvalCodeEntity == null) return ResponseDto.approvalCodeAuthFail();

            approvalCodeEntity.updateExpireDate();
            approvalCodeRepository.save(approvalCodeEntity);

            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

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
            return ResponseDto.redisServerError();
        }
        
        return ResponseDto.success(HttpStatus.OK);
    }
    
}
