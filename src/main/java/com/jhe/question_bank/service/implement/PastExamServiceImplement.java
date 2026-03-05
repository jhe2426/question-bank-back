package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;
import com.jhe.question_bank.common.entity.PastExamEntity;
import com.jhe.question_bank.common.entity.PastExamQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.repository.PastExamQuestionRepository;
import com.jhe.question_bank.repository.PastExamRepository;
import com.jhe.question_bank.repository.QuestionRepository;
import com.jhe.question_bank.service.PastExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastExamServiceImplement implements PastExamService {

    private final PastExamRepository pastExamRepository;
    private final PastExamQuestionRepository pastExamQuestionRepository;
    private final QuestionRepository questionRepository;

    @Override
    public ResponseEntity<? super GetPastExamRoundsResponseDto> getPastExamRounds() {

        List<PastExamEntity> pastExamEntities = new ArrayList<>();

        try {
            
            pastExamEntities = pastExamRepository.findAllByOrderByPastExamRoundAsc();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetPastExamRoundsResponseDto.success(pastExamEntities);
    }

    @Override
    public ResponseEntity<? super GetPastExamQuestionListResponseDto> getPastExamQuestionList(Integer pastExamId) {
        
        List<QuestionEntity> questionEntities = new ArrayList<>();
        Map<Integer, PastExamQuestionEntity> pastExamMap = new HashMap<>();

        try {

            List<PastExamQuestionEntity> pastExamQuestionEntities = pastExamQuestionRepository.findByPastExamIdOrderByQuestionOrderAsc(pastExamId);
            if (pastExamQuestionEntities == null) return ResponseDto.pastExamQuestionIdNotFound();

            for (PastExamQuestionEntity pastExamQuestionEntity : pastExamQuestionEntities) {

                Integer questionId = pastExamQuestionEntity.getQuestionId();

                pastExamMap.put(questionId, pastExamQuestionEntity);

                QuestionEntity questionEntity = questionRepository.findByQuestionId(questionId);
                if (questionEntity != null) {
                    questionEntities.add(questionEntity);
                }
                
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetPastExamQuestionListResponseDto.success(questionEntities, pastExamMap);
    }
    
}
