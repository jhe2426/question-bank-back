package com.jhe.question_bank.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jhe.question_bank.common.dto.request.auth.SignUpRequestDto;

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
    private String approvalCode;
    private Integer currentMockExamRound;
    private Integer currentKillerExamRound;
    private String phoneNumber;
    private String name;
    private String birthDate; 
    private String address;
    private Integer universityId;
    private String gender;
    private String registeredAt;

    public UserEntity(SignUpRequestDto dto) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        this.userId = dto.getUserId();
        this.password = dto.getPassword();
        this.approvalCode = dto.getApprovalCode();
        this.currentMockExamRound = 1;
        this.currentKillerExamRound = 1;
        this.phoneNumber = dto.getPhoneNumber();
        this.name = dto.getName();
        this.birthDate = dto.getBirthDate();
        this.address = dto.getAddress();
        this.universityId = dto.getUniversityId();
        this.gender = dto.getGender();
        this.registeredAt = now.format(dateTimeFormatter);
    }
}
