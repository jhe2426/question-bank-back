package com.jhe.question_bank.service.admin;

import com.jhe.question_bank.common.vo.StoredImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@Service
public class AdminImageStorageService {
    @Value("${file.path}")
    private String uploadPath;

    @Value("${explanation.folder-name}")
    private String explanationFolderName;

    @Value("${mock-question.folder-name}")
    private String mockQuestionFolderName;

    @Value("${past-question.folder-name}")
    private String pastQuestionFolderName;

    private static final long MAX_IMAGE_SIZE =
            10L * 1024 * 1024;

    public StoredImage save(MultipartFile file, String questionType) {

        String folderName = switch (questionType) {
            case "모의고사" -> mockQuestionFolderName;
            case "기출문제" -> pastQuestionFolderName;
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 문제 유형입니다."
            );
        };

        return saveFile(file, folderName, questionType);
    }

    // function: 상세 해설 이미지 저장 //
    public StoredImage saveExplanation(MultipartFile file) {

        return saveFile(file, explanationFolderName, "explanation");
    }
    public void delete(String storageKey) {

        if (storageKey == null || storageKey.isBlank()) {
            return;
        }

        Path root = Path.of(uploadPath)
                .toAbsolutePath()
                .normalize();

        Path filePath = root.resolve(storageKey).normalize();

        if (!filePath.startsWith(root)
                || filePath.equals(root)) {
            throw new IllegalArgumentException("잘못된 파일 삭제 경로입니다.");
        }

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException("이미지 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private StoredImage saveFile(
            MultipartFile file,
            String folderName,
            String imagePathType
    ) {

        // 1. 파일 검증
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("이미지 파일이 존재하지 않습니다.");

        if (file.getSize() > MAX_IMAGE_SIZE)
            throw new IllegalArgumentException("이미지 파일은 10MB 이하만 가능합니다.");

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank())
            throw new IllegalArgumentException("파일명이 존재하지 않습니다.");

        int extensionIndex = originalName.lastIndexOf('.');

        if (extensionIndex < 0)
            throw new IllegalArgumentException("이미지 확장자가 없습니다.");

        String extension = originalName
                .substring(extensionIndex)
                .toLowerCase(Locale.ROOT);

        if (!isAllowedExtension(extension))
            throw new IllegalArgumentException("지원하지 않는 이미지 확장자입니다.");

        validateImageSignature(file, extension);

        // 2. UUID 파일명 생성
        String storedFilename = UUID.randomUUID() + extension;

        Path root = Path.of(uploadPath)
                .toAbsolutePath()
                .normalize();

        Path folderPath = root.resolve(folderName).normalize();

        Path filePath = folderPath.resolve(storedFilename).normalize();

        if (!folderPath.startsWith(root) || folderPath.equals(root))
            throw new IllegalArgumentException("잘못된 이미지 저장 경로입니다.");

        // 3. 이미지 저장
        try {
            Files.createDirectories(folderPath);

            try (InputStream input = file.getInputStream()) {
                Files.copy(input, filePath);
            }

        } catch (IOException e) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException cleanupException) {
                e.addSuppressed(cleanupException);
            }

            throw new UncheckedIOException(
                    "이미지 저장 중 오류가 발생했습니다.", e
            );
        }

        // 4. 이미지 조회 URL 생성
        String storageKey = folderName + "/" + storedFilename;

        String url = "/api/v1/file/"
                + imagePathType
                + "/"
                + storedFilename;

        return new StoredImage(url, storageKey);
    }

    private boolean isAllowedExtension(String extension) {
        return switch (extension) {
            case ".png", ".jpg", ".jpeg", ".webp" -> true;
            default -> false;
        };
    }

    // 이미지 형식을 파일 헤더로 확인
    private void validateImageSignature(
            MultipartFile file,
            String extension
    ) {
        try (InputStream input = file.getInputStream()) {
            byte[] bytes = input.readNBytes(12);

            boolean valid = switch (extension) {
                case ".png" ->
                        bytes.length >= 8
                                && (bytes[0] & 0xff) == 0x89
                                && bytes[1] == 'P'
                                && bytes[2] == 'N'
                                && bytes[3] == 'G';

                case ".jpg", ".jpeg" ->
                        bytes.length >= 3
                                && (bytes[0] & 0xff) == 0xff
                                && (bytes[1] & 0xff) == 0xd8
                                && (bytes[2] & 0xff) == 0xff;

                case ".webp" ->
                        bytes.length >= 12
                                && new String(bytes, 0, 4,
                                java.nio.charset.StandardCharsets.US_ASCII)
                                .equals("RIFF")
                                && new String(bytes, 8, 4,
                                java.nio.charset.StandardCharsets.US_ASCII)
                                .equals("WEBP");

                default -> false;
            };

            if (!valid) throw new IllegalArgumentException("이미지 파일 형식이 올바르지 않습니다.");

        } catch (IOException e) {
            throw new UncheckedIOException("이미지 검증 중 오류가 발생했습니다.", e);
        }
    }
}