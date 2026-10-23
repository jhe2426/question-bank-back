package com.jhe.question_bank.controller.admin;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.service.admin.AdminQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    @PostMapping
    public ResponseEntity<ResponseDto> postQuestions(
            @RequestPart MultipartFile jsonFile,
            @RequestPart(value = "images", required = false)
            List<MultipartFile> images
    ) {

        ResponseEntity<ResponseDto> response = adminQuestionService.postQuestions(jsonFile, images);
        return response;

    }

}
