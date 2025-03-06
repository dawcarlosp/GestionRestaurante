package com.dwes.gestionrestaurante.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
    @Length(min = 3, message = "El nombre de usuario tiene que tener al menos 3 caracteres")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene el formato válido")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Debe proporcionar una contraseña")
    private String password;
    //@NotBlank
    //private String password2;
    @Length(min = 3, message = "{cliente.nombre.longitud}")
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombre;
    private String foto;
}
