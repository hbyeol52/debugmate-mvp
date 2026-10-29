package com.debugmate.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final UserAccountRepository repository; private final PasswordEncoder encoder;
    private final String email; private final String password;
    public AdminInitializer(UserAccountRepository repository, PasswordEncoder encoder,
                            @Value("${app.admin.email}") String email, @Value("${app.admin.password}") String password) {
        this.repository = repository; this.encoder = encoder; this.email = email; this.password = password;
    }
    @Override public void run(String... args) {
        if (password == null || password.isBlank() || repository.existsByEmailIgnoreCase(email)) return;
        UserAccount admin = new UserAccount(); admin.setEmail(email.toLowerCase()); admin.setDisplayName("관리자");
        admin.setPasswordHash(encoder.encode(password)); admin.setRole(UserRole.ADMIN);
        admin.setApprovalStatus(ApprovalStatus.APPROVED); repository.save(admin);
    }
}
