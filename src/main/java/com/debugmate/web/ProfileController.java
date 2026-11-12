package com.debugmate.web;

import com.debugmate.user.ProfileForm;
import com.debugmate.user.UserAccount;
import com.debugmate.user.UserAccountRepository;
import com.debugmate.user.UserRole;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private static final Set<String> ALLOWED_CSS = Set.of("background-color", "color", "border-color", "border-radius", "font-family", "font-size", "line-height");
    private static final Pattern COLOR = Pattern.compile("(?i)(#[0-9a-f]{3,8}|[a-z]{3,20}|rgba?\\([0-9.,% ]+\\))");
    private static final Pattern SIZE = Pattern.compile("[0-9]{1,3}(px|rem|em|%)");
    private static final Pattern FAMILY = Pattern.compile("[a-zA-Z0-9 ,.'\\\"-]{1,120}");
    private final UserAccountRepository repository;

    public ProfileController(UserAccountRepository repository) { this.repository = repository; }

    @GetMapping("/profile")
    String form(Principal principal, Model model) {
        UserAccount user = currentUser(principal);
        if (!model.containsAttribute("profileForm")) model.addAttribute("profileForm", from(user));
        model.addAttribute("user", user);
        return "profile/form";
    }

    @PostMapping("/profile")
    String save(Principal principal, @Valid @ModelAttribute ProfileForm profileForm, BindingResult binding,
                Model model, RedirectAttributes redirect) {
        UserAccount user = currentUser(principal);
        if (user.getRole() == UserRole.ADMIN) return "redirect:/";
        String safeCss = sanitizeCss(profileForm.getCustomCss());
        if (safeCss == null) binding.rejectValue("customCss", "css.invalid", "허용된 CSS 선언만 입력할 수 있습니다.");
        if (binding.hasErrors()) { model.addAttribute("user", user); return "profile/form"; }
        user.setDisplayName(profileForm.getDisplayName().trim());
        user.setCareerSummary(trimToNull(profileForm.getCareerSummary()));
        user.setTechStack(trimToNull(profileForm.getTechStack()));
        user.setInterests(trimToNull(profileForm.getInterests()));
        user.setPortfolioUrl(trimToNull(profileForm.getPortfolioUrl()));
        user.setCustomCss(safeCss);
        user.setProfileCompleted(true);
        repository.save(user);
        redirect.addFlashAttribute("message", "프로필을 저장했습니다.");
        return "redirect:/profile";
    }

    private UserAccount currentUser(Principal principal) {
        return repository.findByEmailIgnoreCase(principal.getName()).orElseThrow();
    }

    private ProfileForm from(UserAccount user) {
        ProfileForm form = new ProfileForm();
        form.setDisplayName(user.getDisplayName()); form.setCareerSummary(user.getCareerSummary());
        form.setTechStack(user.getTechStack()); form.setInterests(user.getInterests());
        form.setPortfolioUrl(user.getPortfolioUrl()); form.setCustomCss(user.getCustomCss());
        return form;
    }

    private String sanitizeCss(String raw) {
        if (raw == null || raw.isBlank()) return "";
        LinkedHashSet<String> declarations = new LinkedHashSet<>();
        for (String part : raw.split(";")) {
            String[] pieces = part.split(":", 2);
            if (pieces.length != 2) return null;
            String property = pieces[0].trim().toLowerCase(Locale.ROOT);
            String value = pieces[1].trim();
            if (!ALLOWED_CSS.contains(property) || !validCssValue(property, value)) return null;
            declarations.add(property + ": " + value);
        }
        return String.join("; ", declarations);
    }

    private boolean validCssValue(String property, String value) {
        return switch (property) {
            case "color", "background-color", "border-color" -> COLOR.matcher(value).matches();
            case "border-radius", "font-size", "line-height" -> SIZE.matcher(value).matches();
            case "font-family" -> FAMILY.matcher(value).matches();
            default -> false;
        };
    }

    private String trimToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
