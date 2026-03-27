package com.jhe.question_bank.service;

import org.springframework.core.io.Resource;

public interface FileService {
    Resource getImageFile(String questionType, String fileName);
}
