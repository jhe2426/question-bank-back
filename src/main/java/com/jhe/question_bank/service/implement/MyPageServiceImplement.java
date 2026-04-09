package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.mypage.PatchUserPasswordRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.mypage.GetLearningRecordListResponseDto;
import com.jhe.question_bank.common.dto.response.mypage.GetUnitGradingResultResponseDto;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.common.entity.UserProblemGroupEntity;
import com.jhe.question_bank.repository.GroupQuestionRepository;
import com.jhe.question_bank.repository.UserProblemGroupRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.repository.resultSet.GetUnitGradingResultSet;
import com.jhe.question_bank.service.MyPageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImplement implements MyPageService{

    private final UserRepository userRepository;
    private final UserProblemGroupRepository userProblemGroupRepository;
    private final GroupQuestionRepository groupQuestionRepository;

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

            boolean isSame = passwordEncoder.matches(newPassword, userPassword);
            if (isSame) return ResponseDto.newPasswordSameAsCurrent();

            String encodedPassword = passwordEncoder.encode(newPassword);
            userEntity.changeUserPassword(encodedPassword);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<? super GetLearningRecordListResponseDto> getLearningRecordList(String userId) {
        
        List<UserProblemGroupEntity> userProblemGroupEntities = new ArrayList<>();
        
        try {
            
            userProblemGroupEntities = userProblemGroupRepository.findByUserIdOrderBySolvedAtAsc(userId);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetLearningRecordListResponseDto.success(userProblemGroupEntities);
    }

    @Override
    public ResponseEntity<? super GetUnitGradingResultResponseDto> getUnitGradingResult(String userId, Integer groupId) {

        List<GetUnitGradingResultSet> resultSets = new ArrayList<>();

        try {
            
            UserProblemGroupEntity userProblemGroupEntity = userProblemGroupRepository.findByGroupId(groupId);
            if (userProblemGroupEntity == null) return ResponseDto.notExistsQuestionGroup();
            if (!userProblemGroupEntity.getUserId().equals(userId)) return ResponseDto.noPermission();
            if (!userProblemGroupEntity.getSourceType().equals("기출문제")) return ResponseDto.notPastExamGroup();

            resultSets = groupQuestionRepository.findUnitGradingResultByGroupId(groupId);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUnitGradingResultResponseDto.success(resultSets);
    }
    
    
}
