package com.marcptr.auth_template.controller.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.marcptr.auth_template.model.User;
import com.marcptr.auth_template.security.CustomUserDetails;
import com.marcptr.auth_template.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/panel")
    public String panel(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        List<User> users=adminService.listUsers();
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Panel de admin");
        model.addAttribute("content", "pages/admin/panel");
        return "layouts/base";
    }
}
