package com.jhe.question_bank.service.implement;

import com.jhe.question_bank.common.dto.request.incorrectquestions.PostIncorrectQuestionGradingRequestDto;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.vo.UserAnswerVO;
import com.jhe.question_bank.repository.*;
import com.jhe.question_bank.service.IncorrectQuestionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.UnexpectedRollbackException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class IncorrectQuestionTransactionTest {

    @Autowired
    private IncorrectQuestionService incorrectQuestionService;

    @Autowired
    private UserIncorrectQuestionRepository incorrectRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("오답 삭제 실패 시 전체 트랜잭션 롤백")
    public void rollbackOnDeleteFailure() {
        // given
        String userId = "jhe123";
        String sourceType = "기출문제";

        assertThat(incorrectRepository.findByUserIdAndQuestionIdAndSourceType(
                userId, 1, sourceType
        )).isNotNull();

        assertThat(incorrectRepository.findByUserIdAndQuestionIdAndSourceType(
                userId, 3, sourceType
        )).isNotNull();

        PostIncorrectQuestionGradingRequestDto dto = new PostIncorrectQuestionGradingRequestDto();
        dto.setSourceType(sourceType);
        dto.setDetailedType(sourceType);
        dto.setUserAnswers(List.of(
                createCorrectAnswer(1, 1),
                createCorrectAnswer(3, 2)
        ));

        // 3번 삭제 시, 1번이 이미 삭제되었는지 확인한 뒤 DB 오류를 발생시킨다.
        jdbcTemplate.execute("""
            CREATE TRIGGER rollback_test_fail_delete
            BEFORE DELETE ON user_incorrect_questions
            FOR EACH ROW 
            BEGIN
                IF OLD.user_id = 'jhe123'
                    AND OLD.question_id = 3
                    AND OLD.source_type = '기출문제' THEN
                    
                    IF EXISTS (
                        SELECT 1
                        FROM user_incorrect_questions
                        WHERE user_id = 'jhe123'
                            AND question_id = 1
                            AND source_type = '기출문제'
                    ) THEN
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT = 'first_delete_not_executed';
                    END IF;
        
                    SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'rollback_test_after_first_delete';
               END IF;
           END
        """);

        try {
            // when
            Throwable thrown = catchThrowable(() ->
                    incorrectQuestionService.postIncorrectQuestionGrade(userId, dto)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(RuntimeException.class)
                    .isNotInstanceOf(UnexpectedRollbackException.class)
                    .hasStackTraceContaining("rollback_test_after_first_delete");

            assertThat(incorrectRepository.findByUserIdAndQuestionIdAndSourceType(
                    userId, 1, sourceType
            )).isNotNull();

            assertThat(incorrectRepository.findByUserIdAndQuestionIdAndSourceType(
                    userId, 3, sourceType
            )).isNotNull();
        } finally {
            jdbcTemplate.execute(
                    "DROP TRIGGER IF EXISTS rollback_test_fail_delete"
            );
        }

    }

    private UserAnswerVO createCorrectAnswer(int questionId, int order) {
        QuestionEntity question = questionRepository.findByQuestionId(questionId);

        assertThat(question).isNotNull();
        assertThat(question.getAnswer()).isNotNull();

        UserAnswerVO answer = new UserAnswerVO();

        ReflectionTestUtils.setField(answer, "questionId", questionId);
        ReflectionTestUtils.setField(answer, "questionOrder", order);
        ReflectionTestUtils.setField(answer, "answer", question.getAnswer());

        return answer;
    }


}
