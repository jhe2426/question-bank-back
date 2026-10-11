package com.jhe.question_bank.controller;

import com.jhe.question_bank.common.dto.request.incorrectquestions.PostIncorrectQuestionGradingRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseCode;
import com.jhe.question_bank.handler.CustomExceptionHandler;
import com.jhe.question_bank.provider.JwtProvider;
import com.jhe.question_bank.repository.UserRepository;
import com.jhe.question_bank.service.IncorrectQuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.UnexpectedRollbackException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncorrectQuestionController.class)
@Import(CustomExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class IncorrectQuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncorrectQuestionService incorrectQuestionService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("오답 채점 중 UnexpectedRollbackException 발생 시 500 DATABASE_ERROR 응답을 반환한다")
    public void postIncorrectQuestionGrade_unexpectedRollback() throws Exception{
        // given
        when(incorrectQuestionService.postIncorrectQuestionGrade(
                nullable(String.class),
                any(PostIncorrectQuestionGradingRequestDto.class)
        )).thenThrow(new UnexpectedRollbackException("테스트 트랜잭션 롤백 예외"));

        String requestBody = """
        {
            "sourceType": "모의고사",
            "detailedType": "오답",
            "userAnswers": [
                {
                    "questionId": 1,
                    "answer": 1
                }
            ]   
        }            
        """;

        // when & then
        mockMvc.perform(post("/api/v1/incorrect-questions/grading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ResponseCode.DATABASE_ERROR));
    }

}