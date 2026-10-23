package com.jhe.question_bank.controller.admin;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/check")
    public ResponseEntity<ResponseDto> checkAdmin() {
        return ResponseDto.success(HttpStatus.OK);
    }
}
