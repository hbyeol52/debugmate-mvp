package com.debugmate.errorcase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "error_cases")
public class ErrorCase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(max = 120, message = "제목은 120자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "프로그래밍 언어를 선택해 주세요.")
    private String language;

    private String framework;

    @Lob @Column(columnDefinition = "LONGTEXT")
    @NotBlank(message = "소스 코드를 입력해 주세요.")
    private String sourceCode;

    @Lob @Column(columnDefinition = "LONGTEXT")
    @NotBlank(message = "오류 로그를 입력해 주세요.")
    private String errorLog;

    @Lob @Column(columnDefinition = "TEXT")
    private String previousAttempts;

    @Enumerated(EnumType.STRING)
    private ErrorStatus status = ErrorStatus.OPEN;

    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }
    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
    public String getErrorLog() { return errorLog; }
    public void setErrorLog(String errorLog) { this.errorLog = errorLog; }
    public String getPreviousAttempts() { return previousAttempts; }
    public void setPreviousAttempts(String previousAttempts) { this.previousAttempts = previousAttempts; }
    public ErrorStatus getStatus() { return status; }
    public void setStatus(ErrorStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
