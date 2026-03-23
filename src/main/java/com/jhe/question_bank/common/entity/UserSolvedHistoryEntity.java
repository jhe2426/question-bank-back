package com.jhe.question_bank.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jhe.question_bank.common.vo.PastExamUserAnswerVO;
import com.jhe.question_bank.common.vo.UserAnswerVO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="userSolvedHistory")
@Table(name="user_solved_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSolvedHistoryEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer solvedId;
    private String userId;
    private Integer questionId;
    private Integer sessionId;
    private Integer userAnswer;
    private boolean isCorrect;
    private String sourceType;
    private String solvedAt;

    public UserSolvedHistoryEntity(PastExamUserAnswerVO userAnswerVO, String userId, boolean isCorrect) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.questionId = userAnswerVO.getQuestionId();
        this.userAnswer = userAnswerVO.getAnswer();
        this.isCorrect = isCorrect;
        this.sourceType = "기출문제";
        this.solvedAt = now.format(dateTimeFormatter);
    }

    public UserSolvedHistoryEntity(UserAnswerVO userAnswerVO, String userId, Integer sessionId, boolean isCorrect, String sourceType) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.questionId = userAnswerVO.getQuestionId();
        this.sessionId = sessionId;
        this.userAnswer = userAnswerVO.getAnswer();
        this.isCorrect = isCorrect;
        this.sourceType = sourceType;
        this.solvedAt = now.format(dateTimeFormatter);
    }

    public UserSolvedHistoryEntity(UserAnswerVO userAnswerVO, String userId, boolean isCorrect, String sourceType) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.questionId = userAnswerVO.getQuestionId();
        this.userAnswer = userAnswerVO.getAnswer();
        this.isCorrect = isCorrect;
        this.sourceType = sourceType;
        this.solvedAt = now.format(dateTimeFormatter);
    }
}
