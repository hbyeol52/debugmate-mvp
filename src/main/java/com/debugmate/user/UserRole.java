package com.debugmate.user;

public enum UserRole {
    JUNIOR("주니어"), SENIOR("시니어"), ADMIN("관리자");
    private final String label;
    UserRole(String label) { this.label = label; }
    public String getLabel() { return label; }
}
