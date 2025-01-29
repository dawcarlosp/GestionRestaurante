package com.dwes.gestionrestaurante.entities;

import com.dwes.gestionrestaurante.validation.HoraFutura;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
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
    @Future(message = "{reserva.fechaReserva.future}")
    private LocalDate fechaReserva;
    @HoraFutura
    private LocalTime horaReserva;
    //Relaciones
    @ManyToOne(targetEntity = Cliente.class)
    private Cliente cliente;
    @ManyToOne(targetEntity = Mesa.class)
    private Mesa mesa;
    private int numeroPersonas;
}
