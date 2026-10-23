package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "sections")
@Table(name = "sections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SectionEntity {

    @Id
    private Integer sectionId;
    private Integer chapterId;
    private Integer sectionNumber;
    private String sectionCode;
    private String sectionName;
}
