package com.dwes.gestionrestaurante.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginResponseDTO {
    private String username;
    private String token;
}
