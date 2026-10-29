package com.debugmate.auth;

import com.debugmate.user.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final AuthService authService; private final AuthenticationManager authManager;
    private final UserAccountRepository repository; private final JwtService jwtService;
    private final String adminEmail;
    public AuthController(AuthService authService, AuthenticationManager authManager, UserAccountRepository repository,
                          JwtService jwtService, @Value("${app.admin.email}") String adminEmail) {
        this.authService = authService; this.authManager = authManager; this.repository = repository;
        this.jwtService = jwtService; this.adminEmail = adminEmail;
    }
    @GetMapping("/login") String login() { return "auth/login"; }
    @PostMapping("/login") String login(@RequestParam String email, @RequestParam String password,
                                         HttpServletRequest request, HttpServletResponse response,
                                         RedirectAttributes redirect) {
        UserAccount account = repository.findByEmailIgnoreCase(email).orElse(null);
        if (account != null && account.getRole() == UserRole.SENIOR && account.getApprovalStatus() != ApprovalStatus.APPROVED) {
            redirect.addFlashAttribute("error", account.getApprovalStatus() == ApprovalStatus.PENDING
                    ? "시니어 가입 승인 대기 중입니다. 관리자 검토 후 로그인할 수 있습니다."
                    : "시니어 가입 신청이 반려되었습니다. 관리자에게 문의해 주세요.");
            return "redirect:/login";
        }
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            account = repository.findByEmailIgnoreCase(email).orElseThrow();
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            new HttpSessionSecurityContextRepository().saveContext(context, request, response);
            ResponseCookie cookie = ResponseCookie.from("debugmate_token", jwtService.issue(account)).httpOnly(true)
                    .secure(false).sameSite("Lax").path("/").maxAge(86400).build();
            response.addHeader("Set-Cookie", cookie.toString());
            return "redirect:/";
        } catch (AuthenticationException ex) {
            redirect.addFlashAttribute("error", "이메일 또는 비밀번호를 확인해 주세요."); return "redirect:/login";
        }
    }
    @GetMapping("/signup") String signup(Model model) {
        if (!model.containsAttribute("signupForm")) model.addAttribute("signupForm", new SignupForm());
        model.addAttribute("adminEmail", adminEmail); return "auth/signup";
    }
    @PostMapping("/signup") String signup(@Valid @ModelAttribute SignupForm signupForm, BindingResult binding,
                                           Model model, RedirectAttributes redirect) {
        model.addAttribute("adminEmail", adminEmail);
        if (binding.hasErrors()) return "auth/signup";
        try {
            UserAccount user = authService.register(signupForm);
            redirect.addFlashAttribute("message", user.getRole() == UserRole.SENIOR
                    ? "시니어 가입 신청을 접수했습니다. 포트폴리오를 관리자 이메일로 보내주세요. 승인 후 로그인할 수 있습니다."
                    : "회원가입이 완료되었습니다. 로그인해 주세요.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) { model.addAttribute("error", ex.getMessage()); return "auth/signup"; }
    }
    @PostMapping("/logout") String logout(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession(false) != null) request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
        response.addHeader("Set-Cookie", ResponseCookie.from("debugmate_token", "").httpOnly(true)
                .sameSite("Lax").path("/").maxAge(0).build().toString()); return "redirect:/";
    }
}
