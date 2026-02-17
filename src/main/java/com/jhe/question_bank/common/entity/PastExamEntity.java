package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="pastExams")
@Table(name="past_exams")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PastExamEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer pastExamId;
    private Integer pastExamRound;
}
