package com.jhe.question_bank.common.entity.pk;

import java.io.Serializable;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterPk implements Serializable {
    @Column(name="chapter_id")
    private Integer chapterId;
    @Column(name="unit_id")
    private Integer unitId;
}
