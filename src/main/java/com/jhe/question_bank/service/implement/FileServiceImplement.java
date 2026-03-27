package com.jhe.question_bank.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.jhe.question_bank.service.FileService;

@Service
public class FileServiceImplement implements FileService {

    @Value("${file.path}")
    private String filePath;
    @Value("${mock-question.folder-name}")
    private String mockQuestionFolderName;
    @Value("${past-question.folder-name}")
    private String pastQuestionFolderName;

    @Override
    public Resource getImageFile(String questionType, String fileName) {

        Resource resource = null;

        try {
            
            String questionFolderName;


            if (questionType.equals("모의고사")) questionFolderName = mockQuestionFolderName;
            else if (questionType.equals("기출문제")) questionFolderName = pastQuestionFolderName;
            else return null;

            String uri = "file:" + filePath + questionFolderName + "/" + fileName;
            resource = new UrlResource(uri);

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

        return resource;
    }
    
}
