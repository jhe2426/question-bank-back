package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jhe.question_bank.common.entity.PastExamQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;

import lombok.Getter;

@Getter
public class QuestionVO {
    private Integer questionId;
    private Integer questionOrder;
    private String questionText;
    private String imageUrl;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    private QuestionVO(QuestionEntity questionEntity, PastExamQuestionEntity pastExamQuestionEntity) {
        this.questionId = questionEntity.getQuestionId();
        this.questionOrder = pastExamQuestionEntity.getQuestionOrder();
        this.questionText = questionEntity.getQuestionText();
        this.imageUrl = questionEntity.getImageUrl();
        this.option1 = questionEntity.getOption1();
        this.option2 = questionEntity.getOption2();
        this.option3 = questionEntity.getOption3();
        this.option4 = questionEntity.getOption4();
    }

    public static List<QuestionVO> getPastExamQuestionList(List<QuestionEntity> questionEntities, Map<Integer, PastExamQuestionEntity> pastExamMap) {

        List<QuestionVO> list = new ArrayList<>();

        for (QuestionEntity questionEntity: questionEntities) {
            PastExamQuestionEntity pastExamQuestionEntity = pastExamMap.get(questionEntity.getQuestionId());

            if (pastExamQuestionEntity != null) {
                QuestionVO vo = new QuestionVO(questionEntity, pastExamQuestionEntity);
                list.add(vo);
            }
        }

        return list;
    }
}
