package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.entity.PastExamEntity;

import lombok.Getter;

@Getter
public class PastExamRoundVO {
    private Integer pastExamId;
    private Integer pastExamRound;

    private PastExamRoundVO(PastExamEntity pastExamEntity) {
        this.pastExamId = pastExamEntity.getPastExamId();
        this.pastExamRound = pastExamEntity.getPastExamRound();
    }

    public static List<PastExamRoundVO> getList(List<PastExamEntity> pastExamEntities) {
        List<PastExamRoundVO> list = new ArrayList<>();
        for (PastExamEntity pastExamEntity: pastExamEntities) {
            PastExamRoundVO vo = new PastExamRoundVO(pastExamEntity);
            list.add(vo);
        }

        return list;
    }
}
