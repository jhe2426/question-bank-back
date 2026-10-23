package com.jhe.question_bank.common.entity;

import com.jhe.question_bank.common.entity.primaryKey.MockPastTagPk;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="mockPastTags")
@Table(name="mock_past_tags")
@IdClass(MockPastTagPk.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MockPastTagEntity {
    @Id
    private Integer questionId;
    @Id
    private Integer pastExamRound;
}
