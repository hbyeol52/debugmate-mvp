package com.debugmate.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false, length = 60)
    private String displayName;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private UserRole role;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ApprovalStatus approvalStatus;
    @Column(columnDefinition = "TEXT")
    private String careerSummary;
    @Column(length = 500)
    private String portfolioUrl;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getCareerSummary() { return careerSummary; }
    public void setCareerSummary(String careerSummary) { this.careerSummary = careerSummary; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
