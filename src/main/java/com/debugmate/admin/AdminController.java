package com.debugmate.admin;

import com.debugmate.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/admin")
public class AdminController {
    private final UserAccountRepository repository;
    public AdminController(UserAccountRepository repository) { this.repository = repository; }
    @GetMapping("/seniors") String seniors(Model model) {
        model.addAttribute("applicants", repository.findByRoleAndApprovalStatusOrderByCreatedAtAsc(UserRole.SENIOR, ApprovalStatus.PENDING));
        return "admin/seniors";
    }
    @PostMapping("/seniors/{id}/approve") @Transactional
    String approve(@PathVariable Long id, RedirectAttributes redirect) {
        UserAccount user = repository.findById(id).orElseThrow();
        if (user.getRole() == UserRole.SENIOR) user.setApprovalStatus(ApprovalStatus.APPROVED);
        redirect.addFlashAttribute("message", "시니어 가입을 승인했습니다. 승인된 사용자는 이제 로그인할 수 있습니다.");
        return "redirect:/";
    }
    @PostMapping("/seniors/{id}/reject") @Transactional
    String reject(@PathVariable Long id, RedirectAttributes redirect) {
        UserAccount user = repository.findById(id).orElseThrow();
        if (user.getRole() == UserRole.SENIOR) user.setApprovalStatus(ApprovalStatus.REJECTED);
        redirect.addFlashAttribute("message", "시니어 가입을 반려했습니다."); return "redirect:/admin/seniors";
    }
}
