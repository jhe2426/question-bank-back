package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.entity.UserProblemGroupEntity;

import lombok.Getter;

@Getter
public class LearningRecordVO {
    private Integer groupId;
    private String solvedDate;
    private String detailedType;
    private Integer totalScore;

    private LearningRecordVO(UserProblemGroupEntity userProblemGroupEntity) {
        this.groupId = userProblemGroupEntity.getGroupId();
        this.solvedDate = extractSolvedDate(userProblemGroupEntity.getSolvedAt());
        this.detailedType = userProblemGroupEntity.getDetailedType();
        this.totalScore = userProblemGroupEntity.getTotalScore();
    }

    public static List<LearningRecordVO> getList(List<UserProblemGroupEntity> userProblemGroupEntities) {
        List<LearningRecordVO> list = new ArrayList<>();

        for (UserProblemGroupEntity userProblemGroupEntity: userProblemGroupEntities) {
            LearningRecordVO vo = new LearningRecordVO(userProblemGroupEntity);
            list.add(vo);
        }

        return list;
    }

    private String extractSolvedDate(String solvedAt) {
        return solvedAt.substring(0, 10);
    }
}
