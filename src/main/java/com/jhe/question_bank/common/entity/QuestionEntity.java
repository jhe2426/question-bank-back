package com.jhe.question_bank.common.entity;

import com.jhe.question_bank.common.dto.request.admin.AdminQuestionDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Integer sectionId;
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

    public QuestionEntity(
            AdminQuestionDto dto,
            Integer detailExplanationId,
            String imageUrl
    ) {
        this.detailExplanationId = detailExplanationId;
        this.sectionId = dto.getSectionId();
        this.questionType = dto.getQuestionType();
        this.questionText = dto.getQuestionText();
        this.imageUrl = imageUrl;

        this.option1 = dto.getOption1();
        this.option2 = dto.getOption2();
        this.option3 = dto.getOption3();
        this.option4 = dto.getOption4();

        this.answer = dto.getAnswer();
        this.shortExplanation = dto.getShortExplanation();
        this.difficulty = dto.getDifficulty();

        this.createdAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
