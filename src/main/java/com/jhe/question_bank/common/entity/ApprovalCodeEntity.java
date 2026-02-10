package com.jhe.question_bank.common.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="approvalCodes")
@Table(name="approval_codes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCodeEntity {
    @Id
    private String approvalCode;
    private String userId;
    private String expireDate;

    public ApprovalCodeEntity(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public void updateUserId(String userId) {
        this.userId = userId;
    }

    public void updateExpireDate() {
        LocalDate nextYear = LocalDate.now().plusYears(1);
        this.expireDate = nextYear.toString();
    }
}
