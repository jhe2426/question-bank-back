package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.killer.exam.PostKillerExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.killer.exam.GetKillerExamQuestionListResponseDto;
import com.jhe.question_bank.common.entity.GroupQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.common.entity.UserIncorrectQuestionEntity;
import com.jhe.question_bank.common.entity.UserProblemGroupEntity;
import com.jhe.question_bank.common.entity.UserSolvedHistoryEntity;
import com.jhe.question_bank.common.vo.UserAnswerVO;
import com.jhe.question_bank.repository.GroupQuestionRepository;
import com.jhe.question_bank.repository.QuestionRepository;
import com.jhe.question_bank.repository.UserIncorrectQuestionRepository;
import com.jhe.question_bank.repository.UserProblemGroupRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.repository.UserSolvedHistoryRepository;
import com.jhe.question_bank.service.KillerExamService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KillerExamServiceImplement implements KillerExamService {
    
    private final GroupQuestionRepository groupQuestionRepository;
    private final UserProblemGroupRepository userProblemGroupRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserSolvedHistoryRepository userSolvedHistoryRepository;
    private final UserIncorrectQuestionRepository userIncorrectQuestionRepository;
    
    @Override
    @Transactional
    public ResponseEntity<? super GetKillerExamQuestionListResponseDto> getKillerExamQuestionList(String userId) {
        List<QuestionEntity> questionEntities = new ArrayList<>();
        int currentRound;

        try {
            
            UserEntity userEntity = userRepository.findByUserId(userId);

            currentRound = userEntity.getCurrentKillerExamRound();
            String questionType = "모의고사";
            String difficulty = "상";

            int totalKillerExamQuestionCount = questionRepository.countByQuestionTypeAndDifficulty(questionType, difficulty);

            String sourceType = "킬러문제";
            int solvedCountInSessionId = userSolvedHistoryRepository.countByUserIdAndSessionIdAndSourceType(userId, currentRound, sourceType);
            
            List<Integer> candidateQuestionIds;

            if (solvedCountInSessionId >= totalKillerExamQuestionCount) {
                currentRound++;
                userEntity.advanceKillerExamRound(currentRound);

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

    @Override
    @Transactional
    public ResponseEntity<? super PostExamGradingResponseDto> postPastExamGrade(String userId, PostKillerExamGradingRequestDto dto) {
        Integer groupId = null;

        try {

            List<UserAnswerVO> userAnswerList = dto.getUserAnswers();
            int sessionId = dto.getSessionId();
            String sourceType = "킬러문제";
            
            int correctCount = 0;

            List<GroupQuestionEntity> groupQuestionEntities = new ArrayList<>();
            List<UserIncorrectQuestionEntity> incorrectQuestionEntities = new ArrayList<>();
            List<UserSolvedHistoryEntity> userSolvedHistoryEntities = new ArrayList<>();

            for (UserAnswerVO userAnswer: userAnswerList) {
                int questionId = userAnswer.getQuestionId();
                int inputAnswer = userAnswer.getAnswer();

                QuestionEntity questionEntity = questionRepository.findByQuestionId(questionId);
                if (questionEntity == null) return ResponseDto.questionIdNotFound();

                boolean isCorrect = questionEntity.getAnswer().equals(inputAnswer);
                if (isCorrect) correctCount++;

                GroupQuestionEntity groupQuestionEntity = new GroupQuestionEntity(userAnswer, isCorrect);
                groupQuestionEntities.add(groupQuestionEntity);

                UserSolvedHistoryEntity userSolvedHistoryEntity = new UserSolvedHistoryEntity(userAnswer, userId, sessionId, isCorrect, sourceType);
                userSolvedHistoryEntities.add(userSolvedHistoryEntity);

                if (!isCorrect) {
                    UserIncorrectQuestionEntity userIncorrectQuestionEntity = userIncorrectQuestionRepository.findByUserIdAndQuestionIdAndSourceType(userId, questionId, sourceType);
                    if (userIncorrectQuestionEntity != null) continue;

                    userIncorrectQuestionEntity = new UserIncorrectQuestionEntity(userId, questionId, sourceType);
                    incorrectQuestionEntities.add(userIncorrectQuestionEntity);
                }
            }

            int totalScore = 0;
            int totalQuestionCount = userAnswerList.size();
            if (totalQuestionCount > 0) totalScore = (int) Math.round((double) correctCount / totalQuestionCount * 100 );

            UserProblemGroupEntity userProblemGroupEntity = new UserProblemGroupEntity(dto, sourceType, userId, totalScore);
            userProblemGroupEntity = userProblemGroupRepository.save(userProblemGroupEntity);
            groupId = userProblemGroupEntity.getGroupId();

            for (GroupQuestionEntity groupQuestionEntity : groupQuestionEntities) {
                groupQuestionEntity.assignGroupId(groupId);
            }

            groupQuestionRepository.saveAll(groupQuestionEntities);
            userIncorrectQuestionRepository.saveAll(incorrectQuestionEntities);
            userSolvedHistoryRepository.saveAll(userSolvedHistoryEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostExamGradingResponseDto.success(groupId);
    }
    
}
