package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.mock.exam.GetMockExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.mock.exam.GetUnitsAndChaptersResponseDto;
import com.jhe.question_bank.common.entity.ChapterEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.entity.UnitEntity;
import com.jhe.question_bank.common.entity.UserEntity;
import com.jhe.question_bank.enums.QuestionDifficulty;
import com.jhe.question_bank.repository.ChapterRepository;
import com.jhe.question_bank.repository.QuestionRepository;
import com.jhe.question_bank.repository.UnitRepository;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.MockExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MockExamServiceImplement implements MockExamService {

    private final UnitRepository unitRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    public ResponseEntity<? super GetUnitsAndChaptersResponseDto> getUnitsAndChapters() {
        List<UnitEntity> unitEntities = new ArrayList<>();
        Map<Integer, List<ChapterEntity>> chpaterMap = new HashMap<>();        

        try {
            
            unitEntities = unitRepository.findAllByOrderByUnitIdAsc();

            List<ChapterEntity> chapterEntities = chapterRepository.findAllByOrderByUnitIdAscChapterNumberAsc();
            for (ChapterEntity chapterEntity: chapterEntities) {
                chpaterMap.computeIfAbsent(chapterEntity.getUnitId(), k -> new ArrayList<>())
                    .add(chapterEntity);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUnitsAndChaptersResponseDto.success(unitEntities, chpaterMap);
    }

    @Override
    public ResponseEntity<? super GetMockExamQuestionListResponseDto> getMockExamQuestionList(String userId, Integer chapterId, QuestionDifficulty difficulty, Boolean hasExplanation) {
        List<QuestionEntity> questionEntities = new ArrayList<>();
        int currentRound;

        try {

            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) return ResponseDto.userNotFound();

            currentRound = userEntity.getCurrentMockExamRound();
            String sourceType = "모의고사";

            List<Integer> candidateQuestionIds = questionRepository.findUnsolvedMockExamQuestionIds(chapterId, difficulty.getDescription(), userId, sourceType, currentRound);
            if (candidateQuestionIds == null || candidateQuestionIds.isEmpty()) {

                boolean isAllSolved = questionRepository.isAllMockExamSolved(userId, sourceType, currentRound);

                if (isAllSolved) {
                    currentRound++;
                    userEntity.updateMockExamRound(currentRound);

                    candidateQuestionIds = questionRepository.findUnsolvedMockExamQuestionIds(chapterId, difficulty.getDescription(), userId, sourceType, currentRound);
                } else {
                    return ResponseDto.noRemainingMockExamQuestions();
                }

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

        return GetMockExamQuestionListResponseDto.success(currentRound, questionEntities, hasExplanation);
    }

}
