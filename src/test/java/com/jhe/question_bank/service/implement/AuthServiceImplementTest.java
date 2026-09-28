package com.jhe.question_bank.service.implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseMessage;
import com.jhe.question_bank.common.exception.BusinessException;
import com.jhe.question_bank.common.exception.ErrorCode;
import com.jhe.question_bank.provider.JwtProvider;
import com.jhe.question_bank.provider.SmsProvider;
import com.jhe.question_bank.repository.ApprovalCodeRepository;
import com.jhe.question_bank.repository.UniversityRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.store.PhoneNumberAuthStore;
import com.jhe.question_bank.store.RefreshTokenStore;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplementTest {

    @Mock private UserRepository userRepository;
    @Mock private ApprovalCodeRepository approvalCodeRepository;
    @Mock private UniversityRepository universityRepository;
    @Mock private JwtProvider jwtProvider;
    @Mock private SmsProvider smsProvider;
    @Mock private RefreshTokenStore refreshTokenStore;
    @Mock private PhoneNumberAuthStore phoneNumberAuthStore;

    @InjectMocks private AuthServiceImplement authService;

    @Test
    void signIn_userNotFound() {
        // Given
        SignInRequestDto request = new SignInRequestDto();
        request.setUserId("unknown-user");
        request.setUserPassword("password");
        when(userRepository.findByUserId("unknown-user")).thenReturn(null);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.signIn(request));

        // Then
        assertSame(ErrorCode.SIGN_IN_FAIL, exception.getErrorCode());
        assertEquals(ResponseMessage.SIGN_IN_FAIL, exception.getErrorCode().getMessage());
        verify(userRepository).findByUserId("unknown-user");
        verify(refreshTokenStore, never()).save(any(), any());
    }

    @Test
    void refreshAccessToken_invalidToken() {
        // Given
        String refreshToken = "invalid-refresh-token";
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(null);

        // When
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> authService.refreshAccessToken(refreshToken)
        );

        // Then
        assertSame(ErrorCode.AUTHENTICATION_FAIL, exception.getErrorCode());
        assertEquals(ResponseMessage.AUTHENTICATION_FAIL, exception.getMessage());
        verify(jwtProvider).validateRefreshToken(refreshToken);
        verifyNoInteractions(refreshTokenStore, userRepository);
        verify(jwtProvider, never()).createAccessToken(any());
    }
}
