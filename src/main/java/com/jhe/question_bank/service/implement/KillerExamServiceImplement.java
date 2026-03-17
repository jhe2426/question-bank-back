package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.killer.exam.GetKillerExamQuestionListResponseDto;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.repository.QuestionRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.repository.UserSolvedHistoryRepository;
import com.jhe.question_bank.service.KillerExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KillerExamServiceImplement implements KillerExamService {
    
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserSolvedHistoryRepository userSolvedHistoryRepository;
    
    @Override
    public ResponseEntity<? super GetKillerExamQuestionListResponseDto> getKillerExamQuestionList(String userId) {
        List<QuestionEntity> questionEntities = new ArrayList<>();
        int currentRound;

        try {
            
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) return ResponseDto.userNotFound();

            currentRound = userEntity.getCurrentKillerExamRound();
            String questionType = "모의고사";
            String difficulty = "상";

            int totalKillerExamQuestionCount = questionRepository.countByQuestionTypeAndDifficulty(questionType, difficulty);

            String sourceType = "기출문제";
            int solvedCountInSessionId = userSolvedHistoryRepository.countByUserIdAndSessionIdAndSourceType(userId, currentRound, sourceType);
            
            List<Integer> candidateQuestionIds;

            if (solvedCountInSessionId >= totalKillerExamQuestionCount) {
                currentRound++;
                userEntity.setKillerExamRound(currentRound);

                candidateQuestionIds = questionRepository.findIdsByTypeAndDifficulty(questionType, difficulty);
            } else {
                List<Integer> allIds = questionRepository.findIdsByTypeAndDifficulty(questionType, difficulty);
                List<Integer> solvedQuestionIds = userSolvedHistoryRepository.findSolvedQuestionIdsBySessionId(userId, currentRound, sourceType);

                allIds.removeAll(solvedQuestionIds);
                candidateQuestionIds = allIds;
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

        return GetKillerExamQuestionListResponseDto.success(questionEntities, currentRound);
    }
    
}
