package com.marcptr.auth_template.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileImageUploadResponse {
    private String status;
    private String message;
    private String imageUrl;

}
