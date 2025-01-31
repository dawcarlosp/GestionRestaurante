package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.DTO.ReservaInfoDTO;
import com.dwes.gestionrestaurante.repositories.ReservaRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservaDetalleController {
    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/reservas/{fecha}")
    public ResponseEntity<List<ReservaInfoDTO>> getReservas(@PathVariable("fecha") String fecha) {
        List<ReservaInfoDTO> resevasDTO = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(fecha, formatter);
        reservaRepository.findAllByFechaReserva(date).forEach(reserva -> {
            resevasDTO.add(
                    ReservaInfoDTO.builder()
                            .cliente(reserva.getCliente().getNombre())
                            .emailCliente(reserva.getCliente().getEmail())
                            .fechaReserva(reserva.getFechaReserva())
                            .horaReserva(reserva.getHoraReserva())
                            .numeroMesa(reserva.getMesa().getNumeroMesa())
                            .numeroPersonas(reserva.getNumeroPersonas())
                            .build()
            );
        });
            return ResponseEntity.ok(resevasDTO);
    }
}