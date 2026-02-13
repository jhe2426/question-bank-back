package com.jhe.question_bank.common.dto.response.mock.exam;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.ChapterEntity;
import com.jhe.question_bank.common.entity.UnitEntity;
import com.jhe.question_bank.common.vo.UnitVO;

import lombok.Getter;

@Getter
public class GetUnitsAndChaptersResponseDto  extends ResponseDto {
    private List<UnitVO> units;

    private GetUnitsAndChaptersResponseDto(List<UnitEntity> unitEntities, Map<Integer, List<ChapterEntity>> chpaterMap) {
        this.units = UnitVO.getList(unitEntities, chpaterMap);
    }

    public static ResponseEntity<GetUnitsAndChaptersResponseDto> success(List<UnitEntity> unitEntities, Map<Integer, List<ChapterEntity>> chpaterMap) {
        GetUnitsAndChaptersResponseDto body = new GetUnitsAndChaptersResponseDto(unitEntities, chpaterMap);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
