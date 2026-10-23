package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/check")
    public ResponseEntity<String> check(){
        return ResponseEntity.ok("관리자 인증 성공");
    }

}
