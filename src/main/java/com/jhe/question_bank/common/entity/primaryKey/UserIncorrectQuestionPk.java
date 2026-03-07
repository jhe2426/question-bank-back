package com.jhe.question_bank.common.entity.primaryKey;

import java.io.Serializable;

import jakarta.persistence.Column;

public class UserIncorrectQuestionPk implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "question_id")
    private Integer questionId;
}
