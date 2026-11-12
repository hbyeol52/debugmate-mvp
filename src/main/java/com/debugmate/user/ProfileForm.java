package com.debugmate.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileForm {
    @NotBlank(message = "표시 이름을 입력해 주세요.") @Size(max = 60)
    private String displayName;
    @Size(max = 3000) private String careerSummary;
    @Size(max = 1000) private String techStack;
    @Size(max = 1000) private String interests;
    @Size(max = 500) private String portfolioUrl;
    @Size(max = 2000) private String customCss;

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getCareerSummary() { return careerSummary; }
    public void setCareerSummary(String careerSummary) { this.careerSummary = careerSummary; }
    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }
    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    public String getCustomCss() { return customCss; }
    public void setCustomCss(String customCss) { this.customCss = customCss; }
}
