package com.marcptr.auth_template.controller.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.marcptr.auth_template.security.CustomUserDetails;
import com.marcptr.auth_template.service.UserService;

@RestController
@RequestMapping("/api")
public class UserAPIController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final RememberMeServices rememberMeServices;

    public UserAPIController(UserService userService,
            AuthenticationManager authenticationManager,
            RememberMeServices rememberMeServices) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.rememberMeServices = rememberMeServices;
    }

    @Value("${uploads.profile-image}")
    private String uploadDir;

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

        return ResponseEntity.ok(fileName);
    }
}
