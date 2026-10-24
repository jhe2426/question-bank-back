package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="detailedExplanations")
@Table(name="detailed_explanations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailedExplanationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer detailExplanationId;
    private String content;
    private String imageUrl;
}
