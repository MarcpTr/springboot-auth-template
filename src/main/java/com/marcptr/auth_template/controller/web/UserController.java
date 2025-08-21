package com.marcptr.auth_template.controller.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.marcptr.auth_template.exceptions.ValidationException;
import com.marcptr.auth_template.model.dto.RegisterDTO;
import com.marcptr.auth_template.security.CustomUserDetails;
import com.marcptr.auth_template.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {
    @Value("${uploads.profile-image}")
    private String uploadDir;

    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final RememberMeServices rememberMeServices;

    public UserController(UserService userService,
            AuthenticationManager authenticationManager,
            RememberMeServices rememberMeServices) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.rememberMeServices = rememberMeServices;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("profileImagePath", user.getUser().getProfileImagePath());
        model.addAttribute("profileImagePath", user.getUser().getProfileImagePath());
        model.addAttribute("pageTitle", "Perfil de " + "username");
        model.addAttribute("content", "pages/profile");
        return "layouts/base";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard de " + "username");
        model.addAttribute("content", "pages/dashboard");
        return "layouts/base";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("pageTitle", "Inicia sesion");
        model.addAttribute("content", "pages/login");
        return "layouts/base";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("pageTitle", "Registrate");
        model.addAttribute("content", "pages/register");
        model.addAttribute("registerDTO", new RegisterDTO());
        return "layouts/base";
    }

    @PostMapping("/register")
    public String register(RegisterDTO registerDTO, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.registerUser(registerDTO.getUsername(), registerDTO.getPassword());
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    registerDTO.getUsername(), registerDTO.getPassword());

            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            rememberMeServices.loginSuccess(request, response, authentication);

            return "redirect:/profile";
        } catch (ValidationException ve) {
            Map<String, List<String>> fieldErrors = ve.getFieldErrors();
            for (Map.Entry<String, List<String>> entry : fieldErrors.entrySet()) {
                model.addAttribute(entry.getKey() + "Errors", entry.getValue());
            }
            model.addAttribute("pageTitle", "Registrate");
            model.addAttribute("content", "pages/register");
            return "layouts/base";
        }
    }

    @PostMapping("/user/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(@AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("image") MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            return ResponseEntity.badRequest().body("Formato no soportado. Solo JPG y PNG.");
        }
        long maxSize = 2 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return ResponseEntity.badRequest().body("Archivo demasiado grande. Máximo 2MB.");
        }
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image.getWidth() > 1024 || image.getHeight() > 1024) {
            return ResponseEntity.badRequest().body("Resolución demasiado alta. Máximo 1024x1024.");
        }
        String extension = contentType.equals("image/png") ? ".png" : ".jpg";
        String fileName = "profile_" + user.getUser().getId() + extension;

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        userService.updateImageProfilePath(user.getUsername(), fileName);
        
        return ResponseEntity.ok("Imagen subida correctamente.");
    }

}
