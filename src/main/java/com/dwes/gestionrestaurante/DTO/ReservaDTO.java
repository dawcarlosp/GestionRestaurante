package com.dwes.gestionrestaurante.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private long idMesa;
    private long idCliente;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Future(message = "Lo sentimos, debe seleccionar una fecha futura")
    private LocalDate fechaReserva;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaReserva;
    private int numeroPersonas;
}
