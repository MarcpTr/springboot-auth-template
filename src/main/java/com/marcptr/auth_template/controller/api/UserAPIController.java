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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.marcptr.auth_template.model.dto.ProfileImageUploadResponse;
import com.marcptr.auth_template.security.CustomUserDetails;
import com.marcptr.auth_template.service.UserService;

@RestController
@RequestMapping("/api")
public class UserAPIController {
    @Autowired
    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @Value("${uploads.profile-image}")
    private String uploadDir;
    @Value("${url.profile-image}")
    private String urlProfile;

    @PutMapping("/user/uploadProfileImage")
    public ResponseEntity<Object> uploadProfileImage(@AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("image") MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            return ResponseEntity.badRequest().body(new ProfileImageUploadResponse("error", "Formato no soportado. Solo JPG y PNG.",null));
        }
        long maxSize = 2 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return ResponseEntity.badRequest().body(new ProfileImageUploadResponse("error","Archivo demasiado grande. Máximo 2MB.", null));
        }
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image.getWidth() > 1024 || image.getHeight() > 1024) {
            return ResponseEntity.badRequest().body(new ProfileImageUploadResponse("error","Resolución demasiado alta. Máximo 1024x1024.",null));
        }
        String extension = contentType.equals("image/png") ? ".png" : ".jpg";
        String fileName = "profile_" + user.getUser().getId() + extension;

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        userService.updateProfileImagePath(user.getUsername(), fileName);
        String imageUrl = urlProfile + fileName;
        ProfileImageUploadResponse response = new ProfileImageUploadResponse("success",
                "Imagen de perfil actualizada correctamente", imageUrl);

        return ResponseEntity.ok(response);
    }
}
