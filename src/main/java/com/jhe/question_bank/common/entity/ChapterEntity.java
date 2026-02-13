package com.jhe.question_bank.common.entity;

import com.jhe.question_bank.common.entity.pk.ChapterPk;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="chapters")
@Table(name="chapters")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChapterPk.class)
public class ChapterEntity {
    @Id
    private Integer chapterId;
    @Id
    private Integer unitId;
    private String chapterName;
}
