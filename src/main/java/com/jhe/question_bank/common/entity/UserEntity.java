package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="users")
@Table(name="users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private Integer currentMockExamRound;
    private Integer currentKillerExamRound;
    private String phoneNumber;
    private String name;
    private String birthDate; 
    private String address;
    private Integer universityNumber;
    private String gender;
    private String registeredAt;
}
