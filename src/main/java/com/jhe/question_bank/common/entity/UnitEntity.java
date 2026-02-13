package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="units")
@Table(name="units")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitEntity {
    @Id
    private Integer unitId;
    private String unitName;
}
