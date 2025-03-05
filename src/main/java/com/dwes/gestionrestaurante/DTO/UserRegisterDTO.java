package com.dwes.gestionrestaurante.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
    //@NotBlank
    private String password2;
    private String nombre;
    private String foto;
}
