package com.jhe.question_bank.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;
import com.jhe.question_bank.common.entity.PastExamEntity;
import com.jhe.question_bank.repository.PastExamRepository;
import com.jhe.question_bank.service.PastExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastExamServiceImplement implements PastExamService {

    private final PastExamRepository pastExamRepository;

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
    
}
