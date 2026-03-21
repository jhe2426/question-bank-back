package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.request.past.exam.PostPastExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.PostPastExamGradingResponseDto;
import com.jhe.question_bank.common.entity.GroupQuestionEntity;
import com.jhe.question_bank.common.entity.PastExamEntity;
import com.jhe.question_bank.common.entity.PastExamQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.entity.UserIncorrectQuestionEntity;
import com.jhe.question_bank.common.entity.UserProblemGroupEntity;
import com.jhe.question_bank.common.entity.UserSolvedHistoryEntity;
import com.jhe.question_bank.common.vo.PastExamUserAnswerVO;
import com.jhe.question_bank.repository.GroupQuestionRepository;
import com.jhe.question_bank.repository.PastExamQuestionRepository;
import com.jhe.question_bank.repository.PastExamRepository;
import com.jhe.question_bank.repository.QuestionRepository;
import com.jhe.question_bank.repository.UserIncorrectQuestionRepository;
import com.jhe.question_bank.repository.UserProblemGroupRepository;
import com.jhe.question_bank.repository.UserSolvedHistoryRepository;
import com.jhe.question_bank.service.PastExamService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastExamServiceImplement implements PastExamService {

    private final PastExamRepository pastExamRepository;
    private final PastExamQuestionRepository pastExamQuestionRepository;
    private final QuestionRepository questionRepository;
    private final UserIncorrectQuestionRepository userIncorrectQuestionRepository;
    private final UserProblemGroupRepository userProblemGroupRepository;
    private final GroupQuestionRepository groupQuestionRepository;
    private final UserSolvedHistoryRepository userSolvedHistoryRepository;

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

    @Override
    @Transactional
    public ResponseEntity<? super PostPastExamGradingResponseDto> postPastExamGrade(String userId, PostPastExamGradingRequestDto dto) {
        Integer groupId = null;

        try {

            List<PastExamUserAnswerVO> userAnswerList = dto.getUserAnswers();
            int pastExamId = dto.getPastExamId();
            
            int correctCount = 0;

            List<GroupQuestionEntity> groupQuestionEntities = new ArrayList<>();
            List<UserIncorrectQuestionEntity> incorrectQuestionEntities = new ArrayList<>();
            List<UserSolvedHistoryEntity> userSolvedHistoryEntities = new ArrayList<>();

            PastExamEntity pastExamEntity = pastExamRepository.findByPastExamId(pastExamId);
            if (pastExamEntity == null) return ResponseDto.pastExamQuestionIdNotFound();
            int pastExamRound = pastExamEntity.getPastExamRound();

            for (PastExamUserAnswerVO userAnswer : userAnswerList) {
                int questionId = userAnswer.getQuestionId();
                int inputAnswer = userAnswer.getAnswer();
                String sourceType = "기출문제";

                QuestionEntity questionEntity = questionRepository.findByQuestionId(questionId);
                if (questionEntity == null) return ResponseDto.questionIdNotFound();

                boolean isCorrect = questionEntity.getAnswer().equals(inputAnswer);
                if (isCorrect) correctCount++;

                PastExamQuestionEntity pastExamQuestionEntity = pastExamQuestionRepository.findByQuestionIdAndPastExamId(questionId, pastExamId);
                if (pastExamQuestionEntity == null) return ResponseDto.questionIdNotFound();

                Integer questionOrder = pastExamQuestionEntity.getQuestionOrder();
                GroupQuestionEntity groupQuestionEntity = new GroupQuestionEntity(userAnswer, isCorrect, questionOrder);
                groupQuestionEntities.add(groupQuestionEntity);

                UserSolvedHistoryEntity userSolvedHistoryEntity = new UserSolvedHistoryEntity(userAnswer, userId, isCorrect);
                userSolvedHistoryEntities.add(userSolvedHistoryEntity);

                if (!isCorrect) {
                    UserIncorrectQuestionEntity userIncorrectQuestionEntity = userIncorrectQuestionRepository.findByUserIdAndQuestionIdAndSourceType(userId, questionId, sourceType);
                    if (userIncorrectQuestionEntity != null) continue;

                    userIncorrectQuestionEntity = new UserIncorrectQuestionEntity(userId, questionId, sourceType);
                    incorrectQuestionEntities.add(userIncorrectQuestionEntity);
                }

            }

            int totalScore = correctCount * 2;
            UserProblemGroupEntity userProblemGroupEntity = new UserProblemGroupEntity(dto, userId, pastExamRound, totalScore);
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

        return PostPastExamGradingResponseDto.success(groupId);
    }
    

    
}
