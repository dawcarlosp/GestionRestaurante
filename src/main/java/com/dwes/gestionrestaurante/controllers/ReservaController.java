package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.DTO.ReservaDTO;
import com.dwes.gestionrestaurante.config.JwtTokenProvider;
import com.dwes.gestionrestaurante.entities.Reserva;
import com.dwes.gestionrestaurante.errors.ValidationErrorResponse;
import com.dwes.gestionrestaurante.repositories.ClienteRepository;
import com.dwes.gestionrestaurante.repositories.MesaRepository;
import com.dwes.gestionrestaurante.repositories.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    JwtTokenProvider tokenProvider;
    /**
     * Obtener todas las reservas, provisional
     */
    @GetMapping
    public ResponseEntity<List<Reserva>> getMesas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return ResponseEntity.ok(reservas); // HTTP 200 OK
    }
    /* Eliminar reserva */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id, @RequestHeader("Autorization") String token) {
        Long idUsuario = tokenProvider.getIdFromToken(token);
        return reservaRepository.findById(id)
                .map(reserva -> {
                    if(!reserva.getCliente().getId().equals(idUsuario)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Esta reserva no es tuya, por lo que no la puedes borrar");
                    }
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().build(); // HTTP 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // HTTP 404 Not Found
    }
    /**
     * Crear una nueva reserva
     */
    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody @Valid ReservaDTO nuevaReserva, BindingResult bindingResult) {
        var mesa = mesaRepository.findById(nuevaReserva.getIdMesa());
        var cliente = clienteRepository.findById(nuevaReserva.getIdCliente());

        if (mesa.isEmpty()) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse("Error", "Debe proporcionar una mesa"));
        }
        if (cliente.isEmpty()) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse("Error", "Debe proporcionar un cliente"));
        }
        if (bindingResult.hasErrors()) {
            List<ValidationErrorResponse> errors = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(new ValidationErrorResponse(fieldName, errorMessage));
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Validar solo si la misma mesa ya está ocupada a la misma hora en el mismo día
        boolean reservaExistente = mesa.get().getReservas().stream()
                .anyMatch(reserva -> reserva.getFechaReserva().isEqual(nuevaReserva.getFechaReserva())
                        && reserva.getHoraReserva().equals(nuevaReserva.getHoraReserva())
                        && reserva.getMesa().getId() == nuevaReserva.getIdMesa()); // Corregido

        if (reservaExistente) {
            return ResponseEntity.badRequest().body(new ValidationErrorResponse("Error", "No se puede hacer la reserva, la mesa ya está ocupada en ese horario"));
        }

        Reserva reserva = Reserva.builder()
                .fechaReserva(nuevaReserva.getFechaReserva())
                .horaReserva(nuevaReserva.getHoraReserva())
                .cliente(cliente.get())
                .mesa(mesa.get())
                .numeroPersonas(nuevaReserva.getNumeroPersonas())
                .build();

        Reserva nuevaReservaGuardada = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReservaGuardada);
    }

}
