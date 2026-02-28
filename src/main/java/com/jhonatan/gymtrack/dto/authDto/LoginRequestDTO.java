package com.jhonatan.gymtrack.dto.authDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Data Transfer Object for User Login")
public class LoginRequestDTO {


    @Schema(description = "User's email address", example = "johndoe@example.com")
    @NotBlank
    @Size(min = 1,  max = 100)
    @Email
    private String email;


    @Schema(
            description = "User's password. Must contain at least one uppercase letter, one lowercase letter, one number, and one special character.",
            example = "StrongP@ssw0rd!"
    )
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve ter de 8 à 60 caracteres, incluindo 1 maiúscula, 1 número e 1 caractere especial.")
    @Size(min = 8, max = 60)
    private String password;

}

