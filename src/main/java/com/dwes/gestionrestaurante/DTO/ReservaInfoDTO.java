package com.dwes.gestionrestaurante.DTO;

import com.dwes.gestionrestaurante.entities.Reserva;
import com.dwes.gestionrestaurante.repositories.ReservaRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

/*
Listar las reservas de un día concreto utilizando un DTO donde se muestren los datos
de la reserva, los datos de la mesa y los datos del cliente todo en un mismo objeto
JSON del tipo: {nombre: ”Manolo”, email: ”m@m.com”, fecha_reserva:
”11/11/2025”, numero_mesa: 6, etc.)
 */
@Data
@Builder
@AllArgsConstructor
public class ReservaInfoDTO {
    private String cliente;
    private String emailCliente;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaReserva;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaReserva;
    private int numeroMesa;
    private int numeroPersonas;
}
