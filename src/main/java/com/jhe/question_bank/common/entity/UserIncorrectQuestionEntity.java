package com.jhe.question_bank.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jhe.question_bank.common.entity.primaryKey.UserIncorrectQuestionPk;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="userIncorrectQuestions")
@Table(name="user_incorrect_questions")
@IdClass(UserIncorrectQuestionPk.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserIncorrectQuestionEntity {
    @Id
    private String userId;
    @Id
    private Integer questionId;
    private String sourceType;
    private String solvedAt;

    public UserIncorrectQuestionEntity(String userId, Integer questionId, String sourceType) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 

        this.userId = userId;
        this.questionId = questionId;
        this.sourceType = sourceType;
        this.solvedAt = now.format(dateTimeFormatter);
    }
}
