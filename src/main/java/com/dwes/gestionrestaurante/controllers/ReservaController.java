package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.DTO.ReservaDTO;
import com.dwes.gestionrestaurante.config.JwtTokenProvider;
import com.dwes.gestionrestaurante.entities.Reserva;
import com.dwes.gestionrestaurante.errors.ValidationErrorResponse;
import com.dwes.gestionrestaurante.repositories.ClienteRepository;
import com.dwes.gestionrestaurante.repositories.MesaRepository;
import com.dwes.gestionrestaurante.repositories.ReservaRepository;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);
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
    public ResponseEntity<?> deleteReserva(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            // Asegurarse de que el token no contiene espacios adicionales o prefijos como "Bearer"
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7).trim(); // Elimina el prefijo "Bearer " y los espacios en blanco
            }
            // Verificar que el token es válido
            if (!tokenProvider.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
            }

            // Obtener el ID del usuario autenticado desde el token
            Long idUsuario = tokenProvider.getIdFromToken(token);

            // Buscar la reserva en la base de datos
            return reservaRepository.findById(id)
                    .map(reserva -> {

                        // Verificar que la reserva pertenece al usuario autenticado
                        if (!reserva.getCliente().getId().equals(idUsuario)) {
                            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                    .body("Esta reserva no es tuya, por lo que no la puedes borrar");
                        }

                        // Eliminar la reserva
                        reservaRepository.delete(reserva);
                        return ResponseEntity.noContent().build(); // HTTP 204 No Content
                    })
                    .orElse(ResponseEntity.notFound().build()); // HTTP 404 Not Found si no se encuentra la reserva
        } catch (JwtException e) {
            // Excepción relacionada con el token (por ejemplo, firma inválida)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido: " + e.getMessage());
        } catch (Exception e) {
            // Excepción genérica para otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
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
