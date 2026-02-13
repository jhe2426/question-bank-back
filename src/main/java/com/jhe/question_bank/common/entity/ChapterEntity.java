package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="chapters")
@Table(name="chapters")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterEntity {
    @Id
    private Integer chapterId;
    private Integer unitId;
    private Integer chapterNumber;
    private String chapterName;
}
