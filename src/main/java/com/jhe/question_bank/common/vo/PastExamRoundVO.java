package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.entity.PastExamQuestionEntity;

import lombok.Getter;

@Getter
public class PastExamRoundVO {
    private Integer pastExamQuestionId;
    private Integer pastExamRound;

    private PastExamRoundVO(PastExamQuestionEntity pastExamQuestionEntity) {
        this.pastExamQuestionId = pastExamQuestionEntity.getPastExamQuestionId();
        this.pastExamRound = pastExamQuestionEntity.getPastExamRound();
    }

    public static List<PastExamRoundVO> getList(List<PastExamQuestionEntity> pastExamQuestionEntities) {
        List<PastExamRoundVO> list = new ArrayList<>();
        for (PastExamQuestionEntity pastExamQuestionEntity: pastExamQuestionEntities) {
            PastExamRoundVO vo = new PastExamRoundVO(pastExamQuestionEntity);
            list.add(vo);
        }

        return list;
    }
}
