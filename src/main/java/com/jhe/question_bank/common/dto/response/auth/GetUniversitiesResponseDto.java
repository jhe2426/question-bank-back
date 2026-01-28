package com.jhe.question_bank.common.dto.response.auth;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.UniversityEntity;
import com.jhe.question_bank.common.vo.UniversityVO;

import lombok.Getter;

@Getter
public class GetUniversitiesResponseDto extends ResponseDto {
    private List<UniversityVO> universities;

    private GetUniversitiesResponseDto(List<UniversityEntity> universityEntities) {
        this.universities = UniversityVO.getList(universityEntities);
    }

    public static ResponseEntity<GetUniversitiesResponseDto> success(List<UniversityEntity> universityEntities) {
        GetUniversitiesResponseDto body = new GetUniversitiesResponseDto(universityEntities);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
