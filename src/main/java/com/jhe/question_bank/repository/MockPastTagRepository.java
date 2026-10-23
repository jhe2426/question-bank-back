package com.jhe.question_bank.repository;


import com.jhe.question_bank.common.entity.MockPastTagEntity;
import com.jhe.question_bank.common.entity.SectionEntity;
import com.jhe.question_bank.common.entity.primaryKey.MockPastTagPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockPastTagRepository extends JpaRepository<MockPastTagEntity, MockPastTagPk> {
}
