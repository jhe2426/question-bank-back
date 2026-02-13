package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jhe.question_bank.common.entity.ChapterEntity;
import com.jhe.question_bank.common.entity.UnitEntity;

import lombok.Getter;

@Getter
public class UnitVO {
    private Integer unitId;
    private String unitName;
    private List<ChapterVO> chapters;

    private UnitVO(UnitEntity unitEntity, List<ChapterEntity> chapterEntities) {
        this.unitId = unitEntity.getUnitId();
        this.unitName = unitEntity.getUnitName();
        this.chapters = ChapterVO.getList(chapterEntities);
    }

    public static List<UnitVO> getList(List<UnitEntity> unitEntities, Map<Integer, List<ChapterEntity>> chapterMap) {
        List<UnitVO> list = new ArrayList<>();
        for (UnitEntity unitEntity: unitEntities) {
            List<ChapterEntity> chapterEntities = chapterMap.getOrDefault(unitEntity.getUnitId(), new ArrayList<>());
            UnitVO vo = new UnitVO(unitEntity, chapterEntities);
            list.add(vo);
        }

        return list;
    }
}
