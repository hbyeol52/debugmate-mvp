package com.debugmate.errorcase;

public enum ErrorStatus {
    OPEN("분석 대기"), RESOLVED("해결 완료"), REVIEW_REQUIRED("검토 필요");

    private final String label;
    ErrorStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
