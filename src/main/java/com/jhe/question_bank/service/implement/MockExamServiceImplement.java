package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.mock.exam.GetUnitsAndChaptersResponseDto;
import com.jhe.question_bank.common.entity.ChapterEntity;
import com.jhe.question_bank.common.entity.UnitEntity;
import com.jhe.question_bank.repository.ChapterRepository;
import com.jhe.question_bank.repository.UnitRepository;
import com.jhe.question_bank.service.MockExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MockExamServiceImplement implements MockExamService {

    private final UnitRepository unitRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public ResponseEntity<? super GetUnitsAndChaptersResponseDto> getUnitsAndChapters() {
        List<UnitEntity> unitEntities = new ArrayList<>();
        Map<Integer, List<ChapterEntity>> chpaterMap = new HashMap<>();        

        try {
            
            unitEntities = unitRepository.findAllByOrderByUnitIdAsc();

            List<ChapterEntity> chapterEntities = chapterRepository.findAllByOrderByUnitIdAscChapterIdAsc();
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

}
