package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.entity.UniversityEntity;

import lombok.Getter;

@Getter
public class UniversityVO {
    private Integer universityId;
    private String universityName;

    private UniversityVO(UniversityEntity universityEntity) {
        this.universityId = universityEntity.getUniversityId();
        this.universityName = universityEntity.getUniversityName();
    }

    public static List<UniversityVO> getList(List<UniversityEntity> universityEntities) {
        List<UniversityVO> list = new ArrayList<>();
        for (UniversityEntity universityEntity: universityEntities) {
            UniversityVO vo = new UniversityVO(universityEntity);
            list.add(vo);
        }

        return list;
    }
}
