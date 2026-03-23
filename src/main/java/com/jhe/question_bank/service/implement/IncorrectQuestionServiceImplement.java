package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.incorrectquestion.GetIncorrectQuestionListResponseDto;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.repository.QuestionRepository;
import com.jhe.question_bank.repository.UserIncorrectQuestionRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.IncorrectQuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncorrectQuestionServiceImplement implements IncorrectQuestionService {

    private final UserRepository userRepository;
    private final UserIncorrectQuestionRepository userIncorrectQuestionRepository;
    private final QuestionRepository questionRepository;

    @Override
    public ResponseEntity<? super GetIncorrectQuestionListResponseDto> getIncorrectQuestionList(String userId, String sourceType) {
        
        List<QuestionEntity> questionEntities = new ArrayList<>();

        try {
            
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) return ResponseDto.userNotFound();

            List<Integer> candidateQuestionIds = userIncorrectQuestionRepository.findIdsByUserIdAndSourceType(userId, sourceType);
            if(candidateQuestionIds.isEmpty()) {
                return GetIncorrectQuestionListResponseDto.success(questionEntities);
            }

            Collections.shuffle(candidateQuestionIds);
            List<Integer> selectedQuestionIds = new ArrayList<>();
            int limit = Math.min(candidateQuestionIds.size(), 10);

            for (int index = 0; index < limit; index++) {
                selectedQuestionIds.add(candidateQuestionIds.get(index));
            }

            questionEntities = questionRepository.findAllByQuestionIdIn(selectedQuestionIds);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetIncorrectQuestionListResponseDto.success(questionEntities);
    }
    
}
