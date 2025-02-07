package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.DTO.ReservaDTO;
import com.dwes.gestionrestaurante.entities.Cliente;
import com.dwes.gestionrestaurante.entities.Mesa;
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
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        return mesaRepository.findById(id)
                .map(reserva -> {
                    mesaRepository.delete(reserva);
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
        if(mesa.isEmpty()){
           return ResponseEntity.badRequest().body( new ValidationErrorResponse("Error", "Debe proporcionar una mesa"));
        }
        if(cliente.isEmpty()){
            return ResponseEntity.badRequest().body( new ValidationErrorResponse("Error", "Debe proporcionar un cliente"));
        }
        if(bindingResult.hasErrors()){
            List<ValidationErrorResponse> errors = new ArrayList<>();

            // Iteramos sobre los errores y agregamos detalles más específicos
            for (ObjectError error : bindingResult.getAllErrors()) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();

                // Agregar error específico a la lista
                errors.add(new ValidationErrorResponse(fieldName, errorMessage));
            }
            return ResponseEntity.badRequest().body(errors);
        }
        List<Reserva> reservasMesa = mesa.get().getReservas();
        for(Reserva reservaMesa : reservasMesa){
            LocalDate fechaReserva = reservaMesa.getFechaReserva();
            LocalTime horaReserva = reservaMesa.getHoraReserva();
            LocalTime horaNuevaReserva = nuevaReserva.getHoraReserva();
            //Comprobación de si se trata del mismo día
            if(fechaReserva.isEqual(nuevaReserva.getFechaReserva())){
                Long diferenciaMinutos = Duration.between(horaNuevaReserva, horaReserva).toMinutes();
                // Si la diferencia es inferior a noventa minutos, la reserva no se puede hacer porque se solapan
                if(Math.abs(diferenciaMinutos) < 90){
                    return ResponseEntity.badRequest().body(new ValidationErrorResponse("Error", "No se puede hacer la reserva, la mesa ya está ocupada en ese horario"));
                }
            }
        }
       Reserva reserva =  Reserva.builder()
                .fechaReserva(nuevaReserva.getFechaReserva())
                .horaReserva(nuevaReserva.getHoraReserva())
                .cliente(cliente.get())
                .mesa(mesa.get())
                .numeroPersonas(nuevaReserva.getNumeroPersonas())
                .build();
        Reserva nuevaReservaGuardada = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReservaGuardada); // HTTP 201 Created
    }
}
