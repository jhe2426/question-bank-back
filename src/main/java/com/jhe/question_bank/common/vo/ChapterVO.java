package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.entity.ChapterEntity;

import lombok.Getter;

@Getter
public class ChapterVO {
    private Integer chapterId;
    private Integer unitId;
    private Integer chapterNumber;
    private String chapterName;


    private ChapterVO(ChapterEntity chapterEntity) {
        this.chapterId = chapterEntity.getChapterId();
        this.unitId = chapterEntity.getUnitId();
        this.chapterNumber = chapterEntity.getChapterNumber();
        this.chapterName = chapterEntity.getChapterName();
    }

    public static List<ChapterVO> getList(List<ChapterEntity> chapterEntities) {
        List<ChapterVO> list = new ArrayList<>();
        for (ChapterEntity chapterEntity: chapterEntities) {
            ChapterVO vo = new ChapterVO(chapterEntity);
            list.add(vo);
        }

        return list;
    }
}
