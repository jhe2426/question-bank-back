package com.jhe.question_bank.enums;

public enum QuestionDifficulty {
    HIGH("상"),
    MEDIUM("중"),
    LOW("하");

    private final String description;

    QuestionDifficulty(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
