package com.jhe.question_bank.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jhe.question_bank.common.dto.request.incorrectquestions.PostIncorrectQuestionGradingRequestDto;
import com.jhe.question_bank.common.dto.request.killer.exam.PostKillerExamGradingRequestDto;
import com.jhe.question_bank.common.dto.request.mock.exam.PostMockExamGradingRequestDto;
import com.jhe.question_bank.common.dto.request.past.exam.PostPastExamGradingRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="userProblemGroups")
@Table(name="user_problem_groups")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProblemGroupEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer groupId;
    private String userId;
    private String sourceType;
    private String detailedType;
    private Integer pastExamRound;
    private Integer totalScore;
    private String solvedAt;

    public UserProblemGroupEntity(PostPastExamGradingRequestDto dto, String sourceType, String userId, int pastExamRound, int totalScore) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.sourceType = sourceType;
        this.detailedType = dto.getDetailedType();
        this.pastExamRound = pastExamRound;
        this.totalScore = totalScore;
        this.solvedAt = now.format(dateTimeFormatter);
    }

    public UserProblemGroupEntity(PostKillerExamGradingRequestDto dto, String sourceType, String userId, int totalScore) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.sourceType = sourceType;
        this.detailedType = sourceType;
        this.totalScore = totalScore;
        this.solvedAt = now.format(dateTimeFormatter);
    }

    public UserProblemGroupEntity(PostMockExamGradingRequestDto dto, String sourceType, String userId, int totalScore) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.sourceType = sourceType;
        this.detailedType = dto.getDetailedType();
        this.totalScore = totalScore;
        this.solvedAt = now.format(dateTimeFormatter);
    }

    public UserProblemGroupEntity(PostIncorrectQuestionGradingRequestDto dto, String sourceType, String userId, int totalScore) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.userId = userId;
        this.sourceType = sourceType;
        this.detailedType = dto.getDetailedType();
        this.totalScore = totalScore;
        this.solvedAt = now.format(dateTimeFormatter);
    }
}
