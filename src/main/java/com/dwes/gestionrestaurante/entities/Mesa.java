package com.dwes.gestionrestaurante.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mesas")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "{mesa.numeroMesa.notNull}")
    @Positive(message = "{mesa.numeroMesa.positive}")
    private int numeroMesa;
    private String Descripcion;
    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserva> reservas;
}
