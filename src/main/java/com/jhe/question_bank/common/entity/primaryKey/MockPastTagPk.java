package com.jhe.question_bank.common.entity.primaryKey;

import jakarta.persistence.Column;

import java.io.Serializable;

public class MockPastTagPk implements Serializable {
    @Column(name = "question_id")
    private Integer questionId;
    @Column(name = "past_exam_round")
    private Integer pastExamRound;
}
