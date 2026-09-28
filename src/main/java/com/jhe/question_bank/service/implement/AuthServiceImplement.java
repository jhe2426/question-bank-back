package com.jhe.question_bank.service.implement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.jhe.question_bank.common.exception.BusinessException;
import com.jhe.question_bank.common.exception.ErrorCode;
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

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;
        String csrfToken = null;

        String userId = dto.getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new BusinessException(ErrorCode.SIGN_IN_FAIL);

        String userPassword = dto.getUserPassword();
        String encodedPassword = userEntity.getPassword();
        boolean isMatch = passwordEncoder.matches(userPassword, encodedPassword);
        if (!isMatch) throw new BusinessException(ErrorCode.SIGN_IN_FAIL);

        ApprovalCodeEntity approvalCodeEntity = approvalCodeRepository.findByUserId(userId);
        if (approvalCodeEntity == null) throw new BusinessException(ErrorCode.SIGN_IN_FAIL);

        String expireDate = approvalCodeEntity.getExpireDate();
        LocalDate expiresAt = LocalDate.parse(expireDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate now = LocalDate.now();
        boolean isNotExpired = now.isBefore(expiresAt) || now.isEqual(expiresAt);
        if(!isNotExpired) throw new BusinessException(ErrorCode.AUTHORIZATION_CODE_EXPIRED);

        accessToken = jwtProvider.createAccessToken(userId);
        refreshToken = jwtProvider.createRefreshToken(userId);
        csrfToken = UUID.randomUUID().toString();

        refreshTokenStore.save(userId, refreshToken);

        return SignInResponseDto.success(accessToken, refreshToken, csrfToken);
    }

    @Override
    public ResponseEntity<? super AccessTokenRefreshResponseDto> refreshAccessToken(String refreshToken) {
        
        String userId = jwtProvider.validateRefreshToken(refreshToken);
        if (userId == null) throw new BusinessException(ErrorCode.AUTHENTICATION_FAIL);

        boolean isMatched = refreshTokenStore.matches(userId, refreshToken);
        if (!isMatched) throw new BusinessException(ErrorCode.AUTHENTICATION_FAIL);

        boolean existUser = userRepository.existsById(userId);
        if (!existUser) throw new BusinessException(ErrorCode.AUTHENTICATION_FAIL);

        String accessToken = jwtProvider.createAccessToken(userId);

        return AccessTokenRefreshResponseDto.success(accessToken);
    }

    @Override
    public ResponseEntity<? super GetUniversitiesResponseDto> getUniversities() {

        List<UniversityEntity>  universityEntities = universityRepository.findAllByOrderByUniversityIdAsc();

        return GetUniversitiesResponseDto.success(universityEntities);
    }

    @Override
    public ResponseEntity<ResponseDto> idCheck(IdCheckRequestDto dto) {

        String userId = dto.getUserId();

        boolean isExistUserId = userRepository.existsByUserId(userId);
        if (isExistUserId) throw new BusinessException(ErrorCode.EXISTS_USER_ID);

        return ResponseDto.success(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ResponseDto> phoneNumberAuthCode(PhoneNumberAuthCodeRequestDto dto) {
        
        String phoneNumber = dto.getPhoneNumber();

        boolean isExistedPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
        if (isExistedPhoneNumber) throw new BusinessException(ErrorCode.EXISTS_USER_PHONE_NUMBER);

        boolean isAuthCodeIssued = phoneNumberAuthStore.authCodeExists(phoneNumber);
        if (isAuthCodeIssued) throw new BusinessException(ErrorCode.AUTH_CODE_ALREADY_SENT);

        String authCode = AuthCodeCreator.generatePhoneNumberAuthCode();

        boolean isSendSuccessful = smsProvider.sendPhoneNumberAuthCodeMessage(phoneNumber, authCode);
        if (!isSendSuccessful) throw new BusinessException(ErrorCode.SMS_SEND_FAILED);

        phoneNumberAuthStore.saveAuthCode(phoneNumber, authCode);

        return ResponseDto.success(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDto> phoneNumberAuthCodeVerify(PhoneNumberAuthCodeVerifyRequestDto dto) {

        String phoneNumber = dto.getPhoneNumber();
        String authCode = dto.getAuthCode();

        boolean isAuthCodeValid = phoneNumberAuthStore.isAuthCodeValid(phoneNumber, authCode);
        if (!isAuthCodeValid) throw new BusinessException(ErrorCode.PHONE_NUMBER_AUTH_FAILED);

        phoneNumberAuthStore.verifySave(phoneNumber);

        return ResponseDto.success(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseDto> approvalCode(ApprovalCodeRequestDto dto) {

        String phoneNumber = dto.getPhoneNumber();

        String approvalCode = AuthCodeCreator.generateApprovalCode();

        boolean isSendSuccessful = smsProvider.sendApprovalCodeMessage(phoneNumber, approvalCode);
        if (!isSendSuccessful) throw new BusinessException(ErrorCode.SMS_SEND_FAILED);

        ApprovalCodeEntity approvalCodeEntity = new ApprovalCodeEntity(approvalCode);
        approvalCodeRepository.save(approvalCodeEntity);

        return ResponseDto.success(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDto> approvalCodeVerify(ApprovalCodeVerifyRequestDto dto) {
        
        String userId = dto.getUserId();
        String approvalCode = dto.getApprovalCode();

        boolean isExistedUserId = userRepository.existsByUserId(userId);
        if (isExistedUserId) throw  new BusinessException(ErrorCode.EXISTS_USER_ID);

        ApprovalCodeEntity approvalCodeEntity = approvalCodeRepository.findByApprovalCode(approvalCode);
        if (approvalCodeEntity == null) throw new BusinessException(ErrorCode.APPROVAL_CODE_AUTH_FAILED);

        String approvalCodeExpireDate = approvalCodeEntity.getExpireDate();
        boolean isExistedApprovalCodeExpireDate = approvalCodeExpireDate != null;
        if (isExistedApprovalCodeExpireDate) throw new BusinessException(ErrorCode.USED_APPROVAL_CODE);

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

        return ResponseDto.success(HttpStatus.OK);
    }

    

    @Transactional
    @Override
    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto) {

        String userId = dto.getUserId();
        String phoneNumber = dto.getPhoneNumber();
        String approvalCode = dto.getApprovalCode();
        String password = dto.getPassword();

        boolean isExistUserId = userRepository.existsByUserId(userId);
        if (isExistUserId) throw new BusinessException(ErrorCode.EXISTS_USER_ID);

        boolean isExistedPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
        if (isExistedPhoneNumber) throw new BusinessException(ErrorCode.EXISTS_USER_PHONE_NUMBER);

        boolean isPhoneAuthVerified = phoneNumberAuthStore.isVerified(phoneNumber);
        if (!isPhoneAuthVerified) throw new BusinessException(ErrorCode.PHONE_NUMBER_AUTH_FAILED);

        ApprovalCodeEntity approvalCodeEntity = approvalCodeRepository.findByApprovalCodeAndUserId(approvalCode, userId);
        if (approvalCodeEntity == null) throw new BusinessException(ErrorCode.APPROVAL_CODE_AUTH_FAILED);

        approvalCodeEntity.updateExpireDate();
        approvalCodeRepository.save(approvalCodeEntity);

        String encodedPassword = passwordEncoder.encode(password);
        dto.setPassword(encodedPassword);

        UserEntity userEntity = new UserEntity(dto);
        userRepository.save(userEntity);

        return ResponseDto.success(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseDto> logout(String userId) {
        refreshTokenStore.delete(userId);
        return ResponseDto.success(HttpStatus.OK);
    }
    
}
