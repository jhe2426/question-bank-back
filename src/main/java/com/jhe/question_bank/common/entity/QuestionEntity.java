package com.jhe.question_bank.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="questions")
@Table(name="questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer questionId;
    private Integer detailExplanationId;
    private Integer chapterId;
    private String questionType;
    private String questionText;
    private String imageUrl;

    @Column(name = "option_1")
    private String option1;
    @Column(name = "option_2")
    private String option2;
    @Column(name = "option_3")
    private String option3;
    @Column(name = "option_4")
    private String option4;

    private Integer answer;
    private String shortExplanation;
    private String difficulty;
    private String createdAt;
}
