package com.debugmate.auth;

import com.debugmate.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository; this.passwordEncoder = passwordEncoder;
    }
    @Transactional public UserAccount register(SignupForm form) {
        if (repository.existsByEmailIgnoreCase(form.getEmail())) throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        if (form.getRole() == UserRole.ADMIN) throw new IllegalArgumentException("관리자 계정은 회원가입할 수 없습니다.");
        if (form.getRole() == UserRole.SENIOR && (form.getCareerSummary() == null || form.getCareerSummary().isBlank()))
            throw new IllegalArgumentException("시니어 지원자는 경력 또는 전문 분야를 작성해야 합니다.");
        UserAccount user = new UserAccount();
        user.setDisplayName(form.getDisplayName().trim()); user.setEmail(form.getEmail().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword())); user.setRole(form.getRole());
        user.setApprovalStatus(form.getRole() == UserRole.SENIOR ? ApprovalStatus.PENDING : ApprovalStatus.APPROVED);
        user.setCareerSummary(form.getCareerSummary()); user.setPortfolioUrl(form.getPortfolioUrl());
        return repository.save(user);
    }
}
