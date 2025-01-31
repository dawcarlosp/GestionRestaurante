package com.dwes.gestionrestaurante.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
    private String password2;
    private String foto;
}
