package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.repository.resultSet.GetExamResultSet;

import lombok.Getter;

@Getter
public class ExamResultVO {
    private Integer questionId;
    private Integer questionOrder;
    private String questionText;
    private String imageUrl;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Integer answer;
    private String detailExplanationContent;
    private Integer userAnswer;
    private Integer isCorrect;

    private ExamResultVO(GetExamResultSet resultSet) {
        this.questionId = resultSet.getQuestionId();
        this.questionOrder = resultSet.getQuestionOrder();
        this.questionText = resultSet.getQuestionText();
        this.imageUrl = resultSet.getImageUrl();
        this.option1 = resultSet.getOption1();
        this.option2 = resultSet.getOption2();
        this.option3 = resultSet.getOption3();
        this.option4 = resultSet.getOption4();
        this.answer = resultSet.getAnswer();
        this.detailExplanationContent = resultSet.getDetailExplanationContent();
        this.userAnswer = resultSet.getUserAnswer();
        this.isCorrect = resultSet.getIsCorrect();
    }

    public static List<ExamResultVO> getList(List<GetExamResultSet> examResultSets) {
        List<ExamResultVO> list = new ArrayList<>();
        for (GetExamResultSet resultSet: examResultSets) {
            ExamResultVO vo = new ExamResultVO(resultSet);
            list.add(vo);
        }

        return list;
    }
}
