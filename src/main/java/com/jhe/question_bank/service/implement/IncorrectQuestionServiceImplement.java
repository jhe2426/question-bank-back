package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.incorrectquestions.PostIncorrectQuestionGradingRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.incorrectquestions.GetIncorrectQuestionListResponseDto;
import com.jhe.question_bank.common.entity.GroupQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
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
import com.jhe.question_bank.service.IncorrectQuestionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncorrectQuestionServiceImplement implements IncorrectQuestionService {

    private final UserRepository userRepository;
    private final UserIncorrectQuestionRepository userIncorrectQuestionRepository;
    private final QuestionRepository questionRepository;
    private final UserProblemGroupRepository userProblemGroupRepository;
    private final GroupQuestionRepository groupQuestionRepository;
    private final UserSolvedHistoryRepository userSolvedHistoryRepository;

    @Override
    public ResponseEntity<? super GetIncorrectQuestionListResponseDto> getIncorrectQuestionList(String userId, String sourceType) {
        
        List<QuestionEntity> questionEntities = new ArrayList<>();

        try {

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

    @Override
    @Transactional
    public ResponseEntity<? super PostExamGradingResponseDto> postIncorrectQuestionGrade(String userId, PostIncorrectQuestionGradingRequestDto dto) {
        Integer groupId = null;

        try {
            
            List<UserAnswerVO> userAnswerList = dto.getUserAnswers();
            String sourceType = dto.getSourceType();

            int correctCount = 0;

            List<GroupQuestionEntity> groupQuestionEntities = new ArrayList<>();
            List<UserIncorrectQuestionEntity> incorrectQuestionEntities = new ArrayList<>();
            List<UserSolvedHistoryEntity> userSolvedHistoryEntities = new ArrayList<>();

            for (UserAnswerVO userAnswer: userAnswerList) {
                int questionId = userAnswer.getQuestionId();
                int inputAnswer = userAnswer.getAnswer();

                QuestionEntity questionEntity = questionRepository.findByQuestionId(questionId);
                if (questionEntity == null) return ResponseDto.questionIdNotFound();

                UserIncorrectQuestionEntity userIncorrectQuestionEntity = userIncorrectQuestionRepository.findByUserIdAndQuestionIdAndSourceType(userId, questionId, sourceType);
                if (userIncorrectQuestionEntity == null) return ResponseDto.incorrectQuestionNotExists();

                boolean isCorrect = questionEntity.getAnswer().equals(inputAnswer);
                if (isCorrect) {
                    correctCount++;
                    userIncorrectQuestionRepository.delete(userIncorrectQuestionEntity);
                }

                GroupQuestionEntity groupQuestionEntity = new GroupQuestionEntity(userAnswer, isCorrect);
                groupQuestionEntities.add(groupQuestionEntity);

                
                String incorrectSourceType = "오답문제";
                UserSolvedHistoryEntity userSolvedHistoryEntity = new UserSolvedHistoryEntity(userAnswer, userId, isCorrect, incorrectSourceType);
                userSolvedHistoryEntities.add(userSolvedHistoryEntity);

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
