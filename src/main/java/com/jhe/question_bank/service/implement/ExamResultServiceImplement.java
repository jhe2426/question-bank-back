package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.exception.BusinessException;
import com.jhe.question_bank.common.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.examresult.GetExamResultResponseDto;
import com.jhe.question_bank.common.entity.UserProblemGroupEntity;
import com.jhe.question_bank.repository.GroupQuestionRepository;
import com.jhe.question_bank.repository.UserProblemGroupRepository;
import com.jhe.question_bank.repository.resultSet.GetExamResultSet;
import com.jhe.question_bank.service.ExamResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImplement implements ExamResultService {

    private final UserProblemGroupRepository userProblemGroupRepository;
    private final GroupQuestionRepository groupQuestionRepository;
    
    @Override
    public ResponseEntity<? super GetExamResultResponseDto> getExamResult(String userId, Integer groupId, String filter) {

        if (!filter.equals("all") && !filter.equals("incorrect"))
            throw new BusinessException(ErrorCode.VALIDATION_FAIL);

        String sourceType;
        String detailedType;
        Integer totalScore;
        List<GetExamResultSet> resultSets = new ArrayList<>();

        UserProblemGroupEntity userProblemGroupEntity = userProblemGroupRepository.findByGroupId(groupId);
        if (userProblemGroupEntity == null) throw new BusinessException(ErrorCode.NOT_EXISTS_QUESTION_GROUP);

        if (!userProblemGroupEntity.getUserId().equals(userId)) throw new BusinessException(ErrorCode.NO_PERMISSION);

        sourceType = userProblemGroupEntity.getSourceType();
        detailedType = userProblemGroupEntity.getDetailedType();
        totalScore = userProblemGroupEntity.getTotalScore();
        resultSets = groupQuestionRepository.findExamResultByGroupId(groupId, filter);

        return GetExamResultResponseDto.success(resultSets, sourceType, detailedType, totalScore);
    }
    
}
