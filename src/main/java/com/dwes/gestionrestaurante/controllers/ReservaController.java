package com.dwes.gestionrestaurante.controllers;

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
     * Obtener todos las reservas, provisional
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
    public ResponseEntity<?> createReserva(@RequestBody @Valid Reserva reserva, BindingResult bindingResult) {
        var mesa = mesaRepository.findById(reserva.getMesa().getId());
        var cliente = clienteRepository.findById(reserva.getCliente().getId());
        if(mesa.isEmpty()){
           return ResponseEntity.badRequest().body(reserva);
        }
        if(cliente.isEmpty()){
            return ResponseEntity.badRequest().body("Cliente vacio");
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
            if(reservaMesa.getFechaReserva().isEqual(reserva.getFechaReserva())
            && reservaMesa.getHoraReserva().getHour() == reserva.getHoraReserva().getHour()
            ){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La mesa ya esta reservada para esa hora");
            }
        }
        Reserva nuevaReserva = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva); // HTTP 201 Created
    }
}
