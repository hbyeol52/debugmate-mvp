package com.debugmate.user;

public enum ApprovalStatus {
    APPROVED("승인"), PENDING("승인 대기"), REJECTED("반려");
    private final String label;
    ApprovalStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
