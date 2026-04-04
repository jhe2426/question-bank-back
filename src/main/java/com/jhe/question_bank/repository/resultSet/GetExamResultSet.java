package com.jhe.question_bank.repository.resultSet;

public interface GetExamResultSet {
    Integer getQuestionId();
    Integer getQuestionOrder();
    String getQuestionText();
    String getImageUrl();
    String getOption1();
    String getOption2();
    String getOption3();
    String getOption4();
    Integer getAnswer();
    String getDetailExplanationContent();
    Integer getUserAnswer();
    Integer getIsCorrect();
}
