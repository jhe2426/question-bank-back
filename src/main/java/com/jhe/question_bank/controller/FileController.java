package com.jhe.question_bank.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    
    private final FileService fileService;

    @GetMapping(value="/{questionType}/{fileName}", produces={MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public Resource getImageFile (
        @PathVariable("questionType") String questionType,
        @PathVariable("fileName") String fileName
    ) {
        Resource resource = fileService.getImageFile(questionType, fileName);
        return resource;
    }

}
