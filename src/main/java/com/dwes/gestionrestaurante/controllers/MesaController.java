package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.entities.Mesa;
import com.dwes.gestionrestaurante.repositories.MesaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mesas")
public class MesaController {
    @Autowired
    private MesaRepository mesaRepository;
    /**
     * Actualizar una mesa existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> updateMesa(@PathVariable Long id, @RequestBody @Valid Mesa mesaDetalles) {
        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesa.setNumeroMesa(mesaDetalles.getNumeroMesa());
                    mesa.setDescripcion(mesaDetalles.getDescripcion());
                    mesa.setReservas(mesaDetalles.getReservas());
                    Mesa mesaActualizado = mesaRepository.save(mesa);
                    return ResponseEntity.ok(mesaActualizado); // HTTP 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // HTTP 404 Not Found
    }
    /**
     * Obtener todos las mesas
     */
    @GetMapping
    public ResponseEntity<List<Mesa>> getMesas() {
        List<Mesa> mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas); // HTTP 200 OK
    }
    /**
     * Obtener una mesa por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> getMesaById(@PathVariable Long id) {
        return mesaRepository.findById(id)
                .map(mesa -> ResponseEntity.ok(mesa)) // HTTP 200 OK
                .orElse(ResponseEntity.notFound().build());   // HTTP 404 Not Found
    }
    /**
     * Crear una nueva mesa
     */
    @PostMapping
    public ResponseEntity<Mesa> createMesa(@RequestBody @Valid Mesa mesa) {
        Mesa nuevaMesa = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa); // HTTP 201 Created
    }
    /**
     * Borrar una mesa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMesa(@PathVariable Long id) {
        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesaRepository.delete(mesa);
                    return ResponseEntity.noContent().build(); // HTTP 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // HTTP 404 Not Found
    }
}
