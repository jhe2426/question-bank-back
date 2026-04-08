package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.UserProblemGroupEntity;

@Repository
public interface UserProblemGroupRepository extends JpaRepository<UserProblemGroupEntity, Integer>{
    UserProblemGroupEntity findByGroupId(Integer groupId);
    List<UserProblemGroupEntity> findByUserIdOrderBySolvedAtAsc(String userId);
}
