package com.marcptr.auth_template.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marcptr.auth_template.security.CustomUserDetails;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("pageTitle", "Perfil de " + "username");
        model.addAttribute("content", "pages/profile");
        return "layouts/base";
    }
}
