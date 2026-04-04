package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.List;

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

        if (!filter.equals("all") && !filter.equals("incorrect")) return ResponseDto.validationFail();

        List<GetExamResultSet> resultSets = new ArrayList<>();

        try {

            UserProblemGroupEntity userProblemGroupEntity = userProblemGroupRepository.findByGroupId(groupId);
            if (userProblemGroupEntity == null) return ResponseDto.questionGroupNotExists();

            if (!userProblemGroupEntity.getUserId().equals(userId)) return ResponseDto.noPermission();

            resultSets = groupQuestionRepository.findExamResultByGroupId(groupId, filter);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        
        return GetExamResultResponseDto.success(resultSets);
    }
    
}
