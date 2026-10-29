package com.debugmate.web;

import com.debugmate.errorcase.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
    private final ErrorCaseService service;
    public HomeController(ErrorCaseService service) { this.service = service; }

    @GetMapping("/")
    String dashboard(Model model) {
        model.addAttribute("total", service.countAll());
        model.addAttribute("resolved", service.countResolved());
        model.addAttribute("reviewRequired", service.countReviewRequired());
        model.addAttribute("recentCases", service.findAll(null).stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/errors/new")
    String createForm(Model model) {
        if (!model.containsAttribute("errorCase")) model.addAttribute("errorCase", new ErrorCase());
        return "error-form";
    }

    @PostMapping("/errors")
    String create(@Valid @ModelAttribute ErrorCase errorCase, BindingResult bindingResult, RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) return "error-form";
        ErrorCase saved = service.save(errorCase);
        redirect.addFlashAttribute("message", "오류 기록을 저장했습니다. AI 분석은 아직 연결되지 않았습니다.");
        return "redirect:/errors/" + saved.getId();
    }

    @GetMapping("/errors")
    String history(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("cases", service.findAll(query));
        model.addAttribute("query", query);
        return "error-list";
    }

    @GetMapping("/errors/{id}")
    String detail(@PathVariable Long id, Model model) {
        model.addAttribute("errorCase", service.find(id));
        model.addAttribute("statuses", ErrorStatus.values());
        return "error-detail";
    }

    @PostMapping("/errors/{id}/status")
    String updateStatus(@PathVariable Long id, @RequestParam ErrorStatus status, RedirectAttributes redirect) {
        service.updateStatus(id, status);
        redirect.addFlashAttribute("message", "상태를 변경했습니다.");
        return "redirect:/errors/" + id;
    }

    @PostMapping("/errors/{id}/delete")
    String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.delete(id);
        redirect.addFlashAttribute("message", "오류 게시글을 삭제했습니다.");
        return "redirect:/errors";
    }

    @GetMapping("/about") String about() { return "about"; }
}
