package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.repositories.ClienteRepository;
import com.dwes.gestionrestaurante.repositories.MesaRepository;
import com.dwes.gestionrestaurante.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MesaRepository mesaRepository;
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
}
