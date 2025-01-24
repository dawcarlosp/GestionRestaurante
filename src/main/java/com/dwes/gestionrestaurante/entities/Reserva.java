package com.dwes.gestionrestaurante.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate fechaReserva;
    private LocalTime horaReserva;
    //Relaciones
    @ManyToOne(targetEntity = Cliente.class)
    private Cliente cliente;
    @ManyToOne(targetEntity = Mesa.class)
    private Mesa mesa;
    private int numeroPersonas;
}
