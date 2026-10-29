package com.debugmate.auth;

import com.debugmate.user.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepository repository;
    public CustomUserDetailsService(UserAccountRepository repository) { this.repository = repository; }
    @Override public UserDetails loadUserByUsername(String email) {
        UserAccount user = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        boolean enabled = user.getApprovalStatus() == ApprovalStatus.APPROVED;
        return User.withUsername(user.getEmail()).password(user.getPasswordHash())
                .roles(user.getRole().name()).disabled(!enabled).build();
    }
}
