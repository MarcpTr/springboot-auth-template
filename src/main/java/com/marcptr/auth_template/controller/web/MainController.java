package com.marcptr.auth_template.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("pageTitle", "Pagina principal");
        model.addAttribute("content", "pages/index");
        return "layouts/base";
    }
}
