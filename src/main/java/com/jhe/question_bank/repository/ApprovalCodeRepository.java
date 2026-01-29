package com.jhe.question_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.ApprovalCodeEntity;

@Repository
public interface ApprovalCodeRepository extends JpaRepository<ApprovalCodeEntity, String>{

    boolean existsByApprovalCode(String approvalCode);

    ApprovalCodeEntity findByUserId(String userId);
    
}
