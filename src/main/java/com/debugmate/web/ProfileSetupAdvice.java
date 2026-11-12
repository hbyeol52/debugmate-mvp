package com.debugmate.web;

import com.debugmate.user.UserAccount;
import com.debugmate.user.UserAccountRepository;
import com.debugmate.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ProfileSetupAdvice {
    private final UserAccountRepository repository;
    public ProfileSetupAdvice(UserAccountRepository repository) { this.repository = repository; }

    @ModelAttribute("profileSetupRequired")
    boolean profileSetupRequired(Principal principal, HttpServletRequest request) {
        if (principal == null || request.getRequestURI().startsWith("/profile")) return false;
        return repository.findByEmailIgnoreCase(principal.getName())
                .filter(user -> user.getRole() != UserRole.ADMIN)
                .map(user -> !user.isProfileCompleted())
                .orElse(false);
    }
}
