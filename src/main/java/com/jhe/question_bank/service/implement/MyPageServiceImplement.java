package com.jhe.question_bank.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.mypage.PatchUserPasswordRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.MyPageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImplement implements MyPageService{

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> patchUserPassword(String userId, PatchUserPasswordRequestDto dto) {

        String currentPassword = dto.getCurrentPassword();
        String newPassword = dto.getNewPassword();

        try {

            UserEntity userEntity = userRepository.findByUserId(userId);
            String userPassword = userEntity.getPassword();
            boolean isMatch = passwordEncoder.matches(currentPassword, userPassword);
            if (!isMatch) return ResponseDto.passwordIncorrect();

            String encodedPassword = passwordEncoder.encode(newPassword);
            userEntity.changeUserPassword(encodedPassword);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success(HttpStatus.OK);
    }
    
}
