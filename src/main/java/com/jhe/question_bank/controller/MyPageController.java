package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.request.mypage.PatchUserPasswordRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.service.MyPageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/my-page")
@RequiredArgsConstructor
public class MyPageController {
    
    private final MyPageService myPageService;

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDto> patchUserPassword(
        @AuthenticationPrincipal String userId,
        @RequestBody @Valid PatchUserPasswordRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = myPageService.patchUserPassword(userId, requestBody);
        return response;
    }

}
