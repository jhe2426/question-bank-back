package com.jhe.question_bank.service.admin;

import com.jhe.question_bank.common.dto.request.admin.AdminQuestionDto;
import com.jhe.question_bank.common.dto.request.admin.DetailedExplanationDto;
import com.jhe.question_bank.common.dto.request.admin.PostQuestionsRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.DetailedExplanationEntity;
import com.jhe.question_bank.common.entity.MockPastTagEntity;
import com.jhe.question_bank.common.entity.PastExamQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.vo.StoredImage;
import com.jhe.question_bank.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminQuestionService {

    private final ObjectMapper objectMapper;
    private final Validator validator;

    private final QuestionRepository questionRepository;
    private final DetailedExplanationRepository detailedExplanationRepository;
    private final PastExamQuestionRepository pastExamQuestionRepository;
    private final PastExamRepository pastExamRepository;
    private final MockPastTagRepository mockPastTagRepository;
    private final SectionRepository sectionRepository;

    private final AdminImageStorageService imageStorage;

    @Transactional
    public ResponseEntity<ResponseDto> postQuestions(MultipartFile jsonFile, List<MultipartFile> images) {


        // 1. JSON 파일 파싱
        PostQuestionsRequestDto requestDto = parseJsonFile(jsonFile);

        // 2. DTO 유효성 검증
        validateRequest(requestDto);

        // 3. 이미지 파일 목록 구성
        Map<String, MultipartFile> imageMap =
                createImageMap(images);

        // 4. DB 및 이미지 참조 사전 검증
        for (AdminQuestionDto dto : requestDto.getQuestions()) {

            validateQuestion(dto, imageMap);
        }

        // 5. 롤백 시 삭제할 파일 목록
        List<String> savedFileKeys = new ArrayList<>();

        registerFileRollbackCleanup(savedFileKeys);

        // 6. 문제별 저장
        for (AdminQuestionDto dto
                : requestDto.getQuestions()) {

            // 문제 이미지 저장
            String questionImageUrl = saveImage(
                    dto.getImageFileName(),
                    dto.getQuestionType(),
                    imageMap,
                    savedFileKeys
            );

            // 상세 해설 및 해설 이미지 저장
            Integer detailExplanationId =
                    saveDetailedExplanation(
                            dto.getDetailedExplanation(),
                            imageMap,
                            savedFileKeys
                    );

            // 문제 저장
            QuestionEntity questionEntity =
                    new QuestionEntity(
                            dto,
                            detailExplanationId,
                            questionImageUrl
                    );

            QuestionEntity savedQuestion =
                    questionRepository.save(questionEntity);

            Integer questionId =
                    savedQuestion.getQuestionId();

            // 문제 유형별 추가 저장
            if ("모의고사".equals(dto.getQuestionType())) {

                saveMockPastTags(
                        questionId,
                        dto.getTags()
                );

            } else if ("기출문제".equals(dto.getQuestionType())) {

                savePastExamQuestion(
                        questionId,
                        dto.getPastExamId(),
                        dto.getQuestionOrder()
                );
            }
        }

        return ResponseDto.success(HttpStatus.CREATED);
    }

    // function: JSON 파일 변환 //
    private PostQuestionsRequestDto parseJsonFile(
            MultipartFile jsonFile
    ) {
        if (jsonFile == null || jsonFile.isEmpty()) {
            throw badRequest("JSON 파일이 존재하지 않습니다.");
        }

        try {
            return objectMapper.readValue(
                    jsonFile.getInputStream(),
                    PostQuestionsRequestDto.class
            );
        } catch (IOException e) {
            throw badRequest("JSON 파일 형식이 올바르지 않습니다.");
        }
    }

    // function: DTO 필수값 검증 //
    private void validateRequest(
            PostQuestionsRequestDto dto
    ) {
        if (dto == null) {
            throw badRequest("등록할 데이터가 없습니다.");
        }

        Set<ConstraintViolation<PostQuestionsRequestDto>> violations =
                validator.validate(dto);

        if (!violations.isEmpty()) {
            ConstraintViolation<PostQuestionsRequestDto> violation =
                    violations.iterator().next();

            throw badRequest(
                    violation.getPropertyPath()
                            + ": "
                            + violation.getMessage()
            );
        }
    }

    // function: 업로드 이미지 Map 생성 //
    private Map<String, MultipartFile> createImageMap(
            List<MultipartFile> images
    ) {
        Map<String, MultipartFile> imageMap = new HashMap<>();

        if (images == null) {
            return imageMap;
        }

        for (MultipartFile image : images) {

            String filename = image.getOriginalFilename();

            if (image.isEmpty()
                    || filename == null
                    || filename.isBlank()) {
                throw badRequest("잘못된 이미지 파일입니다.");
            }

            // 동일 파일명 중복 방지
            if (imageMap.putIfAbsent(filename, image) != null) {
                throw badRequest(
                        "중복된 이미지 파일명입니다: " + filename
                );
            }
        }

        return imageMap;
    }

    // function: 문제 데이터 검증 //
    private void validateQuestion(
            AdminQuestionDto dto,
            Map<String, MultipartFile> imageMap
    ) {

        // 섹션 존재 여부
        if (!sectionRepository.existsById(dto.getSectionId())) {
            throw badRequest(
                    "존재하지 않는 섹션입니다: " + dto.getSectionId()
            );
        }

        String questionType = dto.getQuestionType();

        if (!"모의고사".equals(questionType)
                && !"기출문제".equals(questionType)) {
            throw badRequest("지원하지 않는 문제 유형입니다.");
        }

        // 문제 이미지 존재 확인
        validateImageReference(
                dto.getImageFileName(),
                imageMap
        );

        // 상세 해설 이미지 존재 확인
        DetailedExplanationDto explanation = dto.getDetailedExplanation();

        if (explanation != null) {
            validateImageReference(
                    explanation.getImageFileName(),
                    imageMap
            );

            boolean hasContent = explanation.getContent() != null
                    && !explanation.getContent().isBlank();

            boolean hasImage = explanation.getImageFileName() != null
                    && !explanation.getImageFileName().isBlank();

            if (!hasContent && !hasImage) {
                throw badRequest("상세 해설 내용이 비어 있습니다.");
            }
        }

        // 모의고사
        if ("모의고사".equals(questionType)) {
            parseTagRounds(dto.getTags());
            return;
        }

        // 기출문제
        if (dto.getPastExamId() == null
                || dto.getQuestionOrder() == null
                || dto.getQuestionOrder() <= 0) {
            throw badRequest(
                    "기출문제의 회차 및 문제 순서가 올바르지 않습니다."
            );
        }

        if (!pastExamRepository.existsById(dto.getPastExamId())) {
            throw badRequest(
                    "존재하지 않는 기출 회차입니다."
            );
        }

        if (dto.getTags() != null && !dto.getTags().isBlank()) {
            throw badRequest(
                    "기출문제에는 모의고사 태그를 지정할 수 없습니다."
            );
        }
    }

    // function: 이미지 참조 검증 //
    private void validateImageReference(
            String filename,
            Map<String, MultipartFile> imageMap
    ) {
        if (filename == null || filename.isBlank()) {
            return;
        }

        if (!imageMap.containsKey(filename)) {
            throw badRequest(
                    "업로드한 이미지에서 파일을 찾을 수 없습니다: "
                            + filename
            );
        }
    }

    // function: 이미지 저장 //
    private String saveImage(
            String filename,
            String questionType,
            Map<String, MultipartFile> imageMap,
            List<String> savedFileKeys
    ) {

        if (filename == null || filename.isBlank()) {
            return null;
        }

        MultipartFile file = imageMap.get(filename);

        if (file == null) {
            throw badRequest(
                    "이미지를 찾을 수 없습니다: " + filename
            );
        }

        // 기존에 작성한 AdminImageStorage 재사용
        StoredImage storedImage =
                imageStorage.save(file, questionType);

        // DB 저장 실패 시 파일 정리를 위해 보관
        savedFileKeys.add(storedImage.getStorageKey());

        return storedImage.getUrl();
    }

    // function: 상세 해설 저장 //
    private Integer saveDetailedExplanation(
            DetailedExplanationDto dto,
            Map<String, MultipartFile> imageMap,
            List<String> savedFileKeys
    ) {

        if (dto == null) return null;

        String explanationImageUrl = null;

        // 상세 해설 이미지가 존재하는 경우
        if (dto.getImageFileName() != null
                && !dto.getImageFileName().isBlank()) {

            MultipartFile file = imageMap.get(dto.getImageFileName());

            if (file == null) {
                throw new IllegalArgumentException("해설 이미지가 존재하지 않습니다.");
            }

            // 해설 이미지 전용 폴더에 저장
            StoredImage storedImage = imageStorage.saveExplanation(file);

            savedFileKeys.add(storedImage.getStorageKey());

            explanationImageUrl = storedImage.getUrl();
        }

        // 상세 해설 DB 저장
        DetailedExplanationEntity entity =
                new DetailedExplanationEntity(
                        dto.getContent(),
                        explanationImageUrl
                );

        DetailedExplanationEntity savedEntity =
                detailedExplanationRepository.save(entity);

        return savedEntity.getDetailExplanationId();
    }

    // function: 기출문제 회차 연결 정보 저장 //
    private void savePastExamQuestion(
            Integer questionId,
            Integer pastExamId,
            Integer questionOrder
    ) {

        PastExamQuestionEntity entity =
                new PastExamQuestionEntity(
                        questionId,
                        pastExamId,
                        questionOrder
                );

        pastExamQuestionRepository.save(entity);
    }

    // function: 모의고사 기출 회차 태그 저장 //
    private void saveMockPastTags(
            Integer questionId,
            String tags
    ) {

        List<Integer> rounds = parseTagRounds(tags);

        if (rounds.isEmpty()) {
            return;
        }

        List<MockPastTagEntity> entities = rounds.stream()
                .map(round ->
                        new MockPastTagEntity(questionId, round)
                )
                .toList();

        mockPastTagRepository.saveAll(entities);
    }

    // function: "35,36,37" 문자열을 정수 목록으로 변환 //
    private List<Integer> parseTagRounds(String tags) {

        if (tags == null || tags.isBlank()) {
            return List.of();
        }

        Set<Integer> rounds = new LinkedHashSet<>();

        for (String value : tags.split(",", -1)) {

            int round;

            try {
                round = Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                throw badRequest(
                        "올바르지 않은 기출 회차 태그: " + value
                );
            }

            if (round <= 0) {
                throw badRequest(
                        "기출 회차는 1 이상이어야 합니다."
                );
            }

            if (!rounds.add(round)) {
                throw badRequest(
                        "중복된 기출 회차 태그입니다: " + round
                );
            }
        }

        return new ArrayList<>(rounds);
    }

    // function: DB 롤백 시 저장된 이미지 삭제 //
    private void registerFileRollbackCleanup(
            List<String> savedFileKeys
    ) {

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCompletion(int status) {

                                if (status == STATUS_COMMITTED) {
                                    return;
                                }

                                for (String storageKey : savedFileKeys) {
                                    try {
                                        imageStorage.delete(storageKey);
                                    } catch (Exception e) {
                                        log.error(
                                                "이미지 롤백 정리 실패: {}",
                                                storageKey,
                                                e
                                        );
                                    }
                                }
                            }
                        }
                );
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

}
