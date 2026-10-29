package com.debugmate.auth;

import com.debugmate.user.UserRole;
import jakarta.validation.constraints.*;

public class SignupForm {
    @NotBlank(message = "이름을 입력해 주세요.") @Size(max = 60)
    private String displayName;
    @NotBlank(message = "이메일을 입력해 주세요.") @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해 주세요.") @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;
    @NotNull(message = "회원 유형을 선택해 주세요.")
    private UserRole role = UserRole.JUNIOR;
    @Size(max = 3000)
    private String careerSummary;
    @Size(max = 500)
    private String portfolioUrl;

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getCareerSummary() { return careerSummary; }
    public void setCareerSummary(String careerSummary) { this.careerSummary = careerSummary; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
}
